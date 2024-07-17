package com.projectbasic.quoteworker

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url
import kotlin.random.Random

interface QuoteApi{
    @GET()
    suspend fun fetchQuote(@Url url: String="https://dummyjson.com/quotes/${Random.nextInt(1,1000)}"):Response<NewQuotes>

    companion object{
        val retrofitInstance=Retrofit.Builder()
            .baseUrl("https://api.quotable.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuoteApi::class.java)

    }
}
