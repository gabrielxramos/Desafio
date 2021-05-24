package br.com.gabrielramos.desafiosouthsystem.data.remote.model

import com.google.gson.annotations.SerializedName

class AmountResponse {

    data class AmountResponse (
        @SerializedName("amount")
        var amount : Int
    )

}