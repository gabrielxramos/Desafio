package br.com.gabrielramos.desafiosouthsystem.presenter.features.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.gabrielramos.desafiosouthsystem.domain.repositories.HomeRepository
import br.com.gabrielramos.desafiosouthsystem.presenter.features.home.HomeViewState
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepository): BaseViewModel() {
    private val _states = MutableLiveData<HomeViewState>()
    val states: LiveData<HomeViewState>
        get() = _states

    fun loadAmount(token: String) {
        launch {
            _states.value = HomeViewState.ShowLoading(true)
            repository.doRequestGetAmount(
                token,
                { amountResponse ->
                    _states.value = HomeViewState.ShowAmount(amountResponse)
                    _states.value = HomeViewState.ShowLoading(false)
                },
                {
                    _states.value = HomeViewState.ShowError(true)
                    _states.value = HomeViewState.ShowLoading(false)
                })
        }
    }

    fun loadStatement(token: String, limit: Int, offset: Int) {
        launch {
            _states.value = HomeViewState.ShowLoading(true)
            repository.doRequestGetStatement(
                token,
                limit,
                offset,
                { itemsResponse ->
                    _states.value = HomeViewState.ShowStatement(itemsResponse)
                    _states.value = HomeViewState.ShowLoading(false)
                },
                {
                    _states.value = HomeViewState.ShowError(true)
                    _states.value = HomeViewState.ShowLoading(false)
                })
        }
    }

    fun loadStatementDetails(token: String, id: String) {
        launch {
            _states.value = HomeViewState.ShowLoading(true)
            repository.doRequestGetDetailsStatement(
                token,
                id,
                { detailsResponse ->
                    _states.value = HomeViewState.ShowStatementDetails(detailsResponse)
                    _states.value = HomeViewState.ShowLoading(false)
                },
                {
                    _states.value = HomeViewState.ShowError(true)
                    _states.value = HomeViewState.ShowLoading(false)
                })
        }
    }
}