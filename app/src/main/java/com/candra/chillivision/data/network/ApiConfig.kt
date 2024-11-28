package com.candra.chillivision.data.network

import com.candra.chillivision.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        private const val Base_URL = BuildConfig.BASE_URL
        // private const val Base_URL_MODEL = BuildConfig.BASE_URL_MODEL

        private fun getService(url: String): ApiService {
            val loggingInterceptor = HttpLoggingInterceptor()
                .setLevel(
                    HttpLoggingInterceptor
                        .Level.BODY
                )

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }

        fun getAPIService(): ApiService {
            return getService(Base_URL)
        }

        //fun getAPIModelService() : ApiService {
        //return getService(Base_URL_MODEL)
        //}
    }

}