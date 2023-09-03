package com.example.common.utils

import com.example.common.ui.main_dashboard.configurationOfSaldo
import java.text.NumberFormat
import java.util.Locale


fun String.toIntSafe() =
    if (this.isNotEmpty() && this.filter { it.isDigit() }.isNotEmpty())
        this.toInt()
    else
        0

fun String.currency() : String {
    if (this.filter { it.isDigit() }.isNotEmpty()) {
//        val formatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US).also {
//            it.maximumFractionDigits = 0
//        }
        val nf = NumberFormat.getInstance(Locale("sk", "SK"))
        nf.isGroupingUsed = true

        return nf.format(this.toDouble()) + configurationOfSaldo.value.currentCurrency
    } else {
        return "0" + configurationOfSaldo.value.currentCurrency
    }

}
