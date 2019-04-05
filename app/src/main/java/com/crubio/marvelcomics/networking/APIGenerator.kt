package com.crubio.marvelcomics.networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIGenerator {
    private var mRetrofit: Retrofit? = null

    private val retrofit: Retrofit?
        get() {
            if (mRetrofit == null) {
                mRetrofit = Retrofit.Builder()
                    .baseUrl(MarvelComicsAPI.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return mRetrofit
        }

    val marvelComicsAPI: MarvelComicsAPI?
        get() = retrofit?.create(MarvelComicsAPI::class.java)
}