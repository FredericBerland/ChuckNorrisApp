package com.example.chucknorrisapp

import io.reactivex.Single
import retrofit2.http.GET

interface JokeApiService {

    @GET("https://api.chucknorris.io/jokes/random/")
    fun giveMeAJoke() : Single<Joke>

}