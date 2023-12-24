package com.example.massapp.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

// Interfaz que define los puntos finales de la API relacionados con las operaciones de tarjetas
interface CardService {
    // Obtiengo la información de la tarjeta, incluido su estado de validez
    @GET("/card/valid/{card}")
    fun getBalance(
        @Header("Authorization") authToken: String,
        @Path("card") cardNumber: String
    ): Call<CardResponse>

    // Obtiengo los detalles específicos del saldo de la tarjeta, como el saldo actual y virtual
    @GET("/card/getBalance/{card}")
    fun getBalanceDetails(
        @Header("Authorization") authToken: String,
        @Path("card") cardNumber: String
    ): Call<BalanceResponse>

}