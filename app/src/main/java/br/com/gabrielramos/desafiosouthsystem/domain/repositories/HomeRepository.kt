package br.com.gabrielramos.desafiosouthsystem.domain.repositories

import br.com.gabrielramos.desafiosouthsystem.data.remote.model.AmountResponse
import br.com.gabrielramos.desafiosouthsystem.data.remote.model.DetailsResponse
import br.com.gabrielramos.desafiosouthsystem.data.remote.model.ItemsResponse

interface HomeRepository {
    suspend fun doRequestGetAmount(token: String, success : (AmountResponse) -> Unit, failure: () -> Unit)
    suspend fun doRequestGetStatement(token: String, limit: Int, offset: Int, success : (ItemsResponse) -> Unit, failure: () -> Unit)
    suspend fun doRequestGetDetailsStatement(token: String, id: String, success : (DetailsResponse) -> Unit, failure: () -> Unit)
}