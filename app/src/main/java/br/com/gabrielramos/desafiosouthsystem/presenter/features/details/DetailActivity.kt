package br.com.gabrielramos.desafiosouthsystem.presenter.features.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.gabrielramos.desafiosouthsystem.R
import br.com.gabrielramos.desafiosouthsystem.data.remote.model.DetailsResponse
import br.com.gabrielramos.desafiosouthsystem.presenter.extensions.formatCurrency
import br.com.gabrielramos.desafiosouthsystem.presenter.extensions.formatDateAndHour
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val BUNDLE_KEY_DETAILS = "detailsResponse"

        const val YOUR_ACCOUNT = "Sua conta"
        const val YOUR_BANK = "Seu banco"

        const val DONWLOAD = "/Download"
        const val JPG = ".jpg"
        const val TYPE_IMAGE = "image/*"
        const val CHOOSE = "Escolha"

        const val QUALITY = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        populateScreen()
    }

    private fun populateScreen() {
        val detailsResponse= intent.getSerializableExtra(BUNDLE_KEY_DETAILS) as? DetailsResponse

        detailsResponse?.let {
            textViewValue.text = formatCurrency(it.amount, it.type)

            textViewAuthentication.text = it.authentication
            textViewDate.text = formatDateAndHour(it.createdAt)
            textViewType.text = it.description

            it.to?.let { to ->
                textViewReceiver.text = to
            } ?: run {
                textViewReceiver.text = YOUR_ACCOUNT
            }

            it.bankName?.let { bankName ->
                textViewInstitution.text = bankName
            } ?: run {
                textViewInstitution.text = YOUR_BANK
            }
        }
    }
}