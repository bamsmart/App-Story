package com.shinedev.digitalent.network

import com.shinedev.digitalent.BASE_URL
import com.shinedev.digitalent.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkBuilder {
    private fun setupRetrofit(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor().also {
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    fun <S> createService(serviceClass: Class<S>): S {
        return setupRetrofit().create(serviceClass)
    }
}