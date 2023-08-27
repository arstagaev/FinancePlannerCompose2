package com.example.common.models

import kotlinx.datetime.LocalDate

data class ResultSaldo(
    val date: LocalDate? = null, val income: Int, val sum: Int, val expense: Int//, var isForecast: Boolean = false//, val arrayIncome: ArrayList<Int>, val arrayExpense: ArrayList<Int>
)

data class FutureSaldo(
    val income: Int,
    var startForecastDate: LocalDate? = null,
    val sum1: Int,
    val sum2: Int,
    val sum3: Int,
    val expense: Int, var incomes: List<Int>, var expenses: List<Int>,
    var periodHalfYear: Int? = null,
    var periodFirstYear: Int? = null,
    var periodSecondYear: Int? = null,
)