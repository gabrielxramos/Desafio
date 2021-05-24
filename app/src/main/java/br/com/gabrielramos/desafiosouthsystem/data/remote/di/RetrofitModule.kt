package br.com.gabrielramos.desafiosouthsystem.data.remote.di

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitModule {
    fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val BASE_URL = "https://desafio-mobile-bff.herokuapp.com/"
}