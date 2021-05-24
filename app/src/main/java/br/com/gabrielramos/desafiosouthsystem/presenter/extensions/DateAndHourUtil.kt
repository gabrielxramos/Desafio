package br.com.gabrielramos.desafiosouthsystem.presenter.extensions

import java.text.ParseException
import java.text.SimpleDateFormat


    fun formatDateAndHour(createdAt: String): String {
        val FULL_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        val DATE_MONTH_FORMAT = "dd/MM - HH:mm:ss"

        val formatDateString = SimpleDateFormat(FULL_DATE_FORMAT)
        val formatDate = SimpleDateFormat(DATE_MONTH_FORMAT)

        try {
            val date = formatDateString.parse(createdAt)

            return formatDate.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return ""
    }
