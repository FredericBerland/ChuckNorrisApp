package com.example.chucknorrisapp

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.create

@UnstableDefault
object JokeApiServiceFactory {
    private fun returnService() : JokeApiService {
        val url = "https://api.chucknorris.io/jokes/random"
        val fac = Json.asConverterFactory(MediaType.get("application/json"))

        val requestInterface = Retrofit.Builder()
            .baseUrl(url)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(fac)
            .build()

        return requestInterface.create()
    }
}
