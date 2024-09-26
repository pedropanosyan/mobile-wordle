package com.example.wordle.apiManager

import retrofit.Call
import retrofit.http.GET

interface ApiService {

    @GET("word?length=5&lang=es")
    fun getRandomWord(): Call<List<String>>
}