package br.com.gabrielramos.desafiosouthsystem.domain.repositories

import br.com.gabrielramos.desafiosouthsystem.data.remote.api.BankApi
import br.com.gabrielramos.desafiosouthsystem.data.remote.model.AmountResponse
import br.com.gabrielramos.desafiosouthsystem.data.remote.model.DetailsResponse
import br.com.gabrielramos.desafiosouthsystem.data.remote.model.ItemsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeRepositoryImpl(
    private val api: BankApi
) : HomeRepository {

    override suspend fun doRequestGetAmount(token: String, success: (AmountResponse) -> Unit, failure: () -> Unit) {
        val callback = api.getAmount(token = token)

        withContext(Dispatchers.IO) {
            callback.enqueue(object : Callback<AmountResponse> {
                override fun onFailure(call: Call<AmountResponse>, t: Throwable) {
                    failure()
                }

                override fun onResponse(call: Call<AmountResponse>, response: Response<AmountResponse>) {
                    response.body()?.let {
                        success(it)
                    }
                }
            })
        }
    }

    override suspend fun doRequestGetStatement(token: String, limit: Int, offset: Int, success: (ItemsResponse) -> Unit, failure: () -> Unit) {
        val callback = api.getMyStatement(token = token, limit = limit, offset = offset)

        withContext(Dispatchers.IO) {
            callback.enqueue(object : Callback<ItemsResponse> {
                override fun onFailure(call: Call<ItemsResponse>, t: Throwable) {
                    failure()
                }

                override fun onResponse(call: Call<ItemsResponse>, response: Response<ItemsResponse>) {
                    response.body()?.let {
                        success(it)
                    }
                }
            })
        }
    }

    override suspend fun doRequestGetDetailsStatement(token: String, id: String, success: (DetailsResponse) -> Unit, failure: () -> Unit) {
        val callback = api.getStatementDetail(token = token, id = id)

        withContext(Dispatchers.IO) {
            callback.enqueue(object : Callback<DetailsResponse> {
                override fun onFailure(call: Call<DetailsResponse>, t: Throwable) {
                    failure()
                }

                override fun onResponse(call: Call<DetailsResponse>, response: Response<DetailsResponse>) {
                    response.body()?.let {
                        success(it)
                    }
                }
            })
        }
    }
}