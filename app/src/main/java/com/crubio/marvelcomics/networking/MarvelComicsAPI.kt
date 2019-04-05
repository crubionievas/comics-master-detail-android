package com.crubio.marvelcomics.networking

import com.crubio.marvelcomics.entities.HeroesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelComicsAPI {

    @GET("/v1/public/characters?apikey=" + PUBLIC_KEY)
    fun getHeroes(@Query("hash") hash: String,
                  @Query("ts") timestamp: String,
                  @Query("nameStartsWith") nameStartsWith: String?): Call<HeroesResponse>

    companion object {
        const val BASE_URL = "http://gateway.marvel.com/"
        const val PUBLIC_KEY = "a97e89847d934d0d551f6252cb4be16f"
        const val PRIVATE_KEY = "978985e55e35edf030a37de670b4ea650cf2e580"
    }
}