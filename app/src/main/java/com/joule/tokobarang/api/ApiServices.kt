package com.joule.tokobarang.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiServices {
    companion object{
        fun api() : Retrofit{
            val retrofit = Retrofit.Builder()
                .baseUrl("https://hoodwink.medkomtek.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit
        }
    }
}