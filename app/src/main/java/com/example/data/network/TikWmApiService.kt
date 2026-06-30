package com.example.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

interface TikWmApiService {
    @GET("api/")
    suspend fun fetchVideoDetails(
        @Query("url") tiktokUrl: String,
        @Query("hd") hd: Int = 1
    ): TikWmResponse

    companion object {
        private const val BASE_URL = "https://www.tikwm.com/"

        fun create(): TikWmApiService {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(TikWmApiService::class.java)
        }
    }
}
