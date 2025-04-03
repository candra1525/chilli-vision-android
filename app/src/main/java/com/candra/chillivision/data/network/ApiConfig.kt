package com.candra.chillivision.data.network

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

        private val retrofitInstances = ConcurrentHashMap<String, ApiService>()

        fun getAPIService(userPreferences: UserPreferences, apiType: String = "default"): ApiService {
            return retrofitInstances.getOrPut(apiType) {
                createRetrofitInstance(userPreferences, apiType)
            }
        }

        private fun createRetrofitInstance(userPreferences: UserPreferences, apiType: String): ApiService {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val authInterceptor = Interceptor { chain ->
                val req = chain.request()

                val token = runBlocking {
                    userPreferences.tokenFlow.first()
                }

                val requestHeaders = req.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()

                chain.proceed(requestHeaders)
            }

            val client = OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .build()

            val baseUrl = when (apiType) {
                "chatAi" -> BASE_URL_CHAT_AI
                "model" -> BASE_URL_MODEL
                else -> BASE_URL
            }

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}
