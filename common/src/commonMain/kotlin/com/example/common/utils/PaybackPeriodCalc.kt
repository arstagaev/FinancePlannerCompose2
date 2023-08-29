package com.example.common.utils

import kotlinx.datetime.DateTimePeriod

fun generatePaybackPeriod(months: Int): String {
    val a = DateTimePeriod(months = months)

    return if (a.years!= 0){"${a.years} years, "} else ""+"${a.months} months "
}
