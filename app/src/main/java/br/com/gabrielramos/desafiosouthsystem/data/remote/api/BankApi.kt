package br.com.gabrielramos.desafiosouthsystem.data.remote.api

import br.com.gabrielramos.desafiosouthsystem.data.remote.model.AmountResponse
import br.com.gabrielramos.desafiosouthsystem.data.remote.model.DetailsResponse
import br.com.gabrielramos.desafiosouthsystem.data.remote.model.ItemsResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface BankApi {

    @GET("myBalance")
    fun getAmount(
        @Header("token") token: String
    ): Call<AmountResponse>

    @GET("myStatement/{limit}/{offset}")
    fun getMyStatement(
        @Header("token") token: String,
        @Path("limit") limit: Int,
        @Path("offset") offset: Int
    ): Call<ItemsResponse>

    @GET("myStatement/detail/{id}")
    fun getStatementDetail(
        @Header("token") token: String,
        @Path("id") id: String
    ): Call<DetailsResponse>

}