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
import java.util.concurrent.TimeUnit

class ApiConfig {
    companion object {
        private const val Base_URL = BuildConfig.BASE_URL
        // private const val Base_URL_MODEL = BuildConfig.BASE_URL_MODEL

        fun getAPIService(userPreferences: UserPreferences): ApiService {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val authInterceptor = Interceptor { chain ->
                val req = chain.request()

                // Ambil token terbaru dari Flow
                val token = runBlocking {
                    userPreferences.tokenFlow.first() // Selalu ambil token terbaru
                }

                val requestHeaders = req.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()

                chain.proceed(requestHeaders)
            }

            val client = OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS) // Naikkan dari 60 ke 120 detik
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor) // Selalu gunakan token terbaru
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(Base_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }

}