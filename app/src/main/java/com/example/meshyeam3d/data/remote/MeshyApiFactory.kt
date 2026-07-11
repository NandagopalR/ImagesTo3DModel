package com.example.meshyeam3d.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MeshyApiFactory {
    fun create(apiKey: String): MeshyApi {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val client = OkHttpClient.Builder()
//            .addInterceptor { chain ->
//                val request = chain.request().newBuilder()
//                    .addHeader("Authorization", "Bearer $apiKey")
//                    .addHeader("Content-Type", "application/json")
//                    .build()
//                chain.proceed(request)
//            }
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.meshy.ai/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MeshyApi::class.java)
    }
}
