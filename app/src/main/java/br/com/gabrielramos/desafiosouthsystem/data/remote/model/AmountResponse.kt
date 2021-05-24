package br.com.gabrielramos.desafiosouthsystem.data.remote.model

import com.google.gson.annotations.SerializedName


    data class AmountResponse (
        @SerializedName("amount")
        var amount : Int
    )

