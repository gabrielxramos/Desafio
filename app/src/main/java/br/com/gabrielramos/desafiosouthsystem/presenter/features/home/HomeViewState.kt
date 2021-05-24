package br.com.gabrielramos.desafiosouthsystem.presenter.features.home

import br.com.gabrielramos.desafiosouthsystem.data.remote.model.AmountResponse
import br.com.gabrielramos.desafiosouthsystem.data.remote.model.DetailsResponse
import br.com.gabrielramos.desafiosouthsystem.data.remote.model.ItemsResponse

sealed class HomeViewState {
    data class ShowAmount(val amountResponse: AmountResponse) : HomeViewState()
    data class ShowStatement(val itemsResponse: ItemsResponse) : HomeViewState()
    data class ShowStatementDetails(val detailsResponse: DetailsResponse) : HomeViewState()
    data class ShowLoading(val isLoading: Boolean) : HomeViewState()
    data class ShowError(val isRequestError: Boolean) : HomeViewState()
}