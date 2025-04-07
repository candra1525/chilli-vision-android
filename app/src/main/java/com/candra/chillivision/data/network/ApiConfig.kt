package com.candra.chillivision.data.network

import android.util.Log
import com.candra.chillivision.BuildConfig
import com.candra.chillivision.data.preferences.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

class ApiConfig {
    companion object {
        private const val BASE_URL = BuildConfig.BASE_URL
        private const val BASE_URL_CHAT_AI = BuildConfig.BASE_URL_CHAT_AI
        private const val BASE_URL_MODEL = BuildConfig.BASE_URL_MODEL
        private const val BASE_URL_MODEL_NGROK = BuildConfig.BASE_URL_MODEL_NGROK

        private val retrofitInstances = ConcurrentHashMap<String, ApiService>()

        fun getAPIService(
            userPreferences: UserPreferences,
            apiType: String = "default"
        ): ApiService {
            return retrofitInstances.getOrPut(apiType) {
                createRetrofitInstance(userPreferences, apiType)
            }
        }

        private fun createRetrofitInstance(
            userPreferences: UserPreferences,
            apiType: String
        ): ApiService {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val clientBuilder = OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)


//            if (apiType != "model") {
//                val authInterceptor = Interceptor { chain ->
//                    val req = chain.request()
//                    val token = runBlocking {
//                        userPreferences.tokenFlow.first()
//                    }
//
//                    val requestHeaders = req.newBuilder()
//                        .addHeader("Authorization", "Bearer $token")
//                        .build()
//
//                    chain.proceed(requestHeaders)
//                }
//                clientBuilder.addInterceptor(authInterceptor)
//            }

            if (apiType != "model") {
                val authInterceptor = Interceptor { chain ->
                    val token = runBlocking {
                        userPreferences.tokenFlow.first()
                    }
                    val newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                    chain.proceed(newRequest)
                }
                clientBuilder.addInterceptor(authInterceptor)
            }

            // Interceptor khusus untuk ngrok
            if (apiType == "modelNgrok") {
                val ngrokInterceptor = Interceptor { chain ->
                    val original = chain.request()
                    val request = original.newBuilder()
                        .addHeader("ngrok-skip-browser-warning", "true")
                        .method(original.method, original.body)
                        .build()
                    chain.proceed(request)
                }
                clientBuilder.addInterceptor(ngrokInterceptor)
            }


            val baseUrl = when (apiType) {
                "chatAi" -> BASE_URL_CHAT_AI
                "model" -> BASE_URL_MODEL
                "modelNgrok" -> BASE_URL_MODEL_NGROK
                else -> BASE_URL
            }

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }

    }
}
