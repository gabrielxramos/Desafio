package br.com.gabrielramos.desafiosouthsystem.presenter.features.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.gabrielramos.desafiosouthsystem.R
import br.com.gabrielramos.desafiosouthsystem.data.remote.api.BankApi
import br.com.gabrielramos.desafiosouthsystem.data.remote.di.RetrofitModule
import br.com.gabrielramos.desafiosouthsystem.data.remote.model.AmountResponse
import br.com.gabrielramos.desafiosouthsystem.data.remote.model.DetailsResponse
import br.com.gabrielramos.desafiosouthsystem.data.remote.model.ItemsResponse
import br.com.gabrielramos.desafiosouthsystem.data.remote.model.ListItemsResponse
import br.com.gabrielramos.desafiosouthsystem.domain.repositories.HomeRepositoryImpl
import br.com.gabrielramos.desafiosouthsystem.presenter.extensions.formatCurrency
import br.com.gabrielramos.desafiosouthsystem.presenter.features.details.DetailActivity
import br.com.gabrielramos.desafiosouthsystem.presenter.features.home.adapter.RecyclerAdapter
import br.com.gabrielramos.desafiosouthsystem.presenter.features.home.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    var offSet = 0
    var loadInitial = true

    var listaItems =  ArrayList<ListItemsResponse>()
    lateinit var adapterList: RecyclerAdapter

    companion object {
        const val LIMIT = 10
        const val OFFSET = 1
        const val TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"

        const val PREFERENCE_NAME = "PREFERENCE_NAME"
        const val PREFENRECE_KEY_AMOUNT = "showAmount"
        const val BUNDLE_KEY_DETAILS = "detailsResponse"

        const val REQUEST_ERROR = "Algo de errado aconteceu, tente novamente mais tarde!"
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val retrofit = RetrofitModule()
        val retrofitClient = retrofit.getRetrofitInstance()
        val endPoint = retrofitClient.create(BankApi::class.java)
        val repository = HomeRepositoryImpl(endPoint)

        viewModel = HomeViewModel(repository)

        configureRecyclerView()
        handleAmountVisibily()
        setupObserving()
        callGetAmount()
        callGetStatement()
    }

    private fun handleAmountVisibily(){
        val sharedPreference = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        if(sharedPreference.getBoolean(PREFENRECE_KEY_AMOUNT, false)){
            showAmount()
        }else{
            hideAmount()
        }

        imageShowHideAmount.setOnClickListener {
            if (sharedPreference.getBoolean(PREFENRECE_KEY_AMOUNT, false)) {
                hideAmount()
            } else {
                showAmount()
            }
        }
    }

    private fun setListItems(){
        adapterList = RecyclerAdapter(listaItems, baseContext){ id ->
            onClick(id)
        }

        recyclerView.adapter = adapterList

        if(!loadInitial){
            recyclerView.scrollToPosition(listaItems.size - 1)
        }
        loadInitial = false
    }

    private fun showAmount(){
        imageShowHideAmount.setImageResource(R.drawable.eye_close)
        viewWithoutAmount.visibility = INVISIBLE
        textViewAmount.visibility = VISIBLE

        val sharedPreference =  getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putBoolean(PREFENRECE_KEY_AMOUNT, true)
        editor.apply()
    }

    private fun hideAmount() {
        imageShowHideAmount.setImageResource(R.drawable.eye_open)
        viewWithoutAmount.visibility = VISIBLE
        textViewAmount.visibility = INVISIBLE

        val sharedPreference =  getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putBoolean(PREFENRECE_KEY_AMOUNT, false)
        editor.apply()
    }

    private fun configureRecyclerView() {
        with(recyclerView) {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            setHasFixedSize(false)
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    callGetStatement()
                }
            }
        })
    }

    private fun callGetStatement(){
        viewModel.loadStatement(TOKEN, LIMIT, offSet)
        offSet += OFFSET
    }

    private fun callGetAmount() {
        viewModel.loadAmount(TOKEN)
    }

    private fun setupObserving() {
        viewModel.states.observe(this, Observer {state ->
            when(state) {
                is HomeViewState.ShowAmount -> showAmountValue(state.amountResponse)
                is HomeViewState.ShowStatement -> showItemsStatement(state.itemsResponse)
                is HomeViewState.ShowStatementDetails -> goToDetailsActivity(state.detailsResponse)
                is HomeViewState.ShowError -> showError(state.isRequestError)
                is HomeViewState.ShowLoading -> showLoading(state.isLoading)
            }
        })
    }

    private fun showAmountValue(amountResponse: AmountResponse) {
        textViewAmount?.text = formatCurrency(amountResponse.amount)
    }

    private fun showItemsStatement(itemsResponse: ItemsResponse) {
        for (item in itemsResponse.items) {
            listaItems.add(item)
        }

        setListItems()
    }

    private fun showError(isRequestError: Boolean) {
        if (isRequestError) {
            Toast.makeText(this, REQUEST_ERROR, Toast.LENGTH_LONG)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            loading.visibility = VISIBLE
        } else {
            loading.visibility = View.GONE
        }
    }

    private fun goToDetailsActivity(detailsResponse: DetailsResponse) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(BUNDLE_KEY_DETAILS, detailsResponse)
        startActivity(intent)
    }

    private fun onClick(id: String) {
        viewModel.loadStatementDetails(TOKEN, id)
    }
}