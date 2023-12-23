package com.example.massapp.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface CardService {
    @GET("/card/valid/{card}")
    fun getBalance(
        @Header("Authorization") authToken: String,
        @Path("card") cardNumber: String
    ): Call<CardResponse>

    @GET("/card/getBalance/{card}")
    fun getBalanceDetails(
        @Header("Authorization") authToken: String,
        @Path("card") cardNumber: String
    ): Call<BalanceResponse>

}