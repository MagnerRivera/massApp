package com.example.massapp.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Clase que proporciona una instancia de Retrofit para realizar llamadas a la API de tarjetas
class RetrofitClient {
    companion object {
        // URL base de la API
        private const val BASE_URL = "https://osgqhdx2wf.execute-api.us-west-2.amazonaws.com/"

        // Método que crea y configura una instancia de Retrofit con la URL base y el convertidor Gson
        fun create(): CardService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            // Devuelvo una instancia de la interfaz CardService creada por Retrofit
            return retrofit.create(CardService::class.java)
        }
    }
}