package com.example.itunesearch.model

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APICalls {

    @GET("search?")
    fun getMusic(@Query("term") term: String): Call<JsonObject>


}