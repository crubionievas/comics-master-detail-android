package com.crubio.marvelcomics.networking

import com.crubio.marvelcomics.bus.BusProvider
import com.crubio.marvelcomics.entities.HeroesData
import com.crubio.marvelcomics.entities.HeroesResponse
import okhttp3.Headers
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HeroresFetch {
    companion object {
        fun getHeroes() {
            val timestamp = System.currentTimeMillis().toString()
            val hash =
                String(Hex.encodeHex(DigestUtils.md5(timestamp + MarvelComicsAPI.PRIVATE_KEY + MarvelComicsAPI.PUBLIC_KEY)))

            val call = APIGenerator.marvelComicsAPI?.getHeroes(hash, timestamp, null)

            call?.enqueue(object : Callback<HeroesResponse> {
                override fun onResponse(call: Call<HeroesResponse>, response: Response<HeroesResponse>) {
                    // Request successful (status code 200, 201)
                    if (response.isSuccessful) {
                        val heroesResponse = response.body()
                        val heroesData = heroesResponse?.data
                        BusProvider.instance.post(HeroesEvent(heroesData))
                    } else {
                        BusProvider.instance.post(ErrorEvent(response.code(), response.message(), response.headers()))
                    }
                }

                override fun onFailure(call: Call<HeroesResponse>, t: Throwable) {
                    BusProvider.instance.post(FailureEvent(t.message))
                }
            })
        }
    }

    class HeroesEvent(val heroesData: HeroesData?)

    class ErrorEvent(val errorCode: Int, val errorMessage: String, val headers: Headers)

    class FailureEvent(val message: String?)
}