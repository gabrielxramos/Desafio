package br.com.gabrielramos.desafiosouthsystem.presenter.extensions

import java.text.NumberFormat
import java.util.*

fun formatCurrency(amount: Int, type: String = ""): String {
    val COUNTRY = "BR"
    val LANGUAGE = "pt"

    return getSignal(type) + NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY)).format(amount)
}

private fun getSignal(type: String): String {
    val OUT = "OUT"

    if (type.contains(OUT)) {
        return "- "
    }

    return ""
}