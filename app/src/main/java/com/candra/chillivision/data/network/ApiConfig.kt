package com.candra.chillivision.data.network

import com.candra.chillivision.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        private const val Base_URL = BuildConfig.BASE_URL
        // private const val Base_URL_MODEL = BuildConfig.BASE_URL_MODEL

        fun getAPIService(token: String?): ApiService {
            val loggingInterceptor = HttpLoggingInterceptor()
                .setLevel(
                    HttpLoggingInterceptor
                        .Level.BODY
                )

            val authInterceptor = Interceptor { chain ->
                val req = chain.request()
                val requestHeaders = req.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(requestHeaders)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
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