package com.example.common.utils

fun String.toIntSafe() =
    if (this.isNotEmpty() && this.filter { it.isDigit() }.isNotEmpty())
        this.toInt()
    else
        0