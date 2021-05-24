package br.com.gabrielramos.desafiosouthsystem.presenter.features.details

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import br.com.gabrielramos.desafiosouthsystem.R
import br.com.gabrielramos.desafiosouthsystem.data.remote.model.DetailsResponse
import br.com.gabrielramos.desafiosouthsystem.presenter.extensions.formatCurrency
import br.com.gabrielramos.desafiosouthsystem.presenter.extensions.formatDateAndHour
import kotlinx.android.synthetic.main.activity_detail.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

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

        buttonShare.setOnClickListener {
            askPermissions()
        }

        populateScreen()
        viewBackOnclick()
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

    private fun viewBackOnclick() {
        viewBack.setOnClickListener {
            finish()
        }
    }

    private fun askPermissions() {
        if ((ContextCompat.checkSelfPermission(baseContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(baseContext, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            Handler().postDelayed({
                takeScreenshotOfView(constraintMain, constraintMain.height, constraintMain.width)
            }, 1000)

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            }
        }
    }

    private fun takeScreenshotOfView(view: View, height: Int, width: Int) {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)

        val file = bitmapToFile(bitmap)

        file?.let {
            val intent = Intent(Intent.ACTION_SEND)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(it.absolutePath))
            intent.type = TYPE_IMAGE
            startActivity(Intent.createChooser(intent, CHOOSE))
        }
    }

    private fun bitmapToFile(bitmap: Bitmap): File? {
        var filePaht: File? = null
        return try {
            filePaht = File(getExternalFilesDir(null).toString() + DONWLOAD)

            if (!filePaht.exists()) {
                filePaht.mkdirs()
            }

            val file = File(filePaht, Calendar.getInstance().timeInMillis.toString() + JPG)
            file.createNewFile()

            val bytes = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY, bytes)

            FileOutputStream(file).write(bytes.toByteArray())

            file
        } catch (e: Exception) {
            e.printStackTrace()
            filePaht
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Handler().postDelayed({
                takeScreenshotOfView(constraintMain, constraintMain.height, constraintMain.width)
            }, 1000)
        }
    }
}