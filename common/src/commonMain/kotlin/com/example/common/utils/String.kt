package com.example.common.utils

import com.example.common.ui.main_dashboard.configurationOfSaldo
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.abs


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
//        val nf = NumberFormat.getInstance(Locale("sk", "SK")) as DecimalFormat
//        nf.isGroupingUsed = true
//        nf.applyPattern("#,###")

        val nf = DecimalFormat("#,###.##")
        val num = this.toDouble()

        return when {
            abs(num) >= 1000000000 -> String.format("%.3fB", num / 1000000000.0)
            abs(num) >= 1_000_000 -> String.format("%.3fM", num / 1000000.0)
            abs(num) >= 100_000 -> String.format("%.2fK", num / 1000.0)
            else -> num.toString()
        } + configurationOfSaldo.value.currentCurrency

        return nf.format(this.toDouble()) + configurationOfSaldo.value.currentCurrency
    } else {
        return "0" + configurationOfSaldo.value.currentCurrency
    }

}

//fun Double.toCompact() : String