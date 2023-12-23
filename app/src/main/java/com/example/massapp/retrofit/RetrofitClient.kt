package com.example.massapp.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {
        private const val BASE_URL = "https://osgqhdx2wf.execute-api.us-west-2.amazonaws.com/"

        fun create(): CardService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(CardService::class.java)
        }
    }
}