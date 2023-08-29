package com.example.common.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter

fun generateJsonFile(): String {
//    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
//    val current = LocalDateTime.parse("yyyy-MM-dd HH:mm")
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    return "${today.hour}${today.minute}${today.second}_${today.dayOfMonth}${today.monthNumber}.json"

}