package com.example.common

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

actual suspend fun encodeForSave() {
    val json = Json.encodeToString<SaveContainer>(SaveContainer(stateFall))
    val jsonConfig = Json.encodeToString<SaldoConfiguration>(configurationOfSaldo.value)

    println(">>>> ${json}")
    writeToFile(json, File(Dir1,"data.json"))
    writeToFile(jsonConfig, File(Dir1,"config.json"))
}

actual suspend fun decodeFromFile() {
    var json = ""
    try {
        BufferedReader(FileReader(theFileArray)).use { br ->
            var line: String?
            while (br.readLine().also { line = it } != null) {
                json += line
                println(line)
            }
        }
    } catch (e: IOException) {
        println("ERROR ${e.message}")
    }

    var jsonConfig = ""
    try {
        BufferedReader(FileReader(theFileConfig)).use { br ->
            var line: String?
            while (br.readLine().also { line = it } != null) {
                jsonConfig += line
                println(line)
            }
        }
    } catch (e: IOException) {
        println("ERROR ${e.message}")
    }
    println("<>> ${jsonConfig}")

    if (jsonConfig.isNotEmpty()) {
        val decodedConfig = Json.decodeFromString<SaldoConfiguration>(jsonConfig)
        configurationOfSaldo.value =  configurationOfSaldo.value.copy(investmentsAmount = decodedConfig.investmentsAmount, investmentsName = decodedConfig.investmentsName)

    }else {
        encodeForSave()
    }

    if (json.isNotEmpty()) {
        stateFall = Json.decodeFromString<SaveContainer>(json).data
    } else {
        encodeForSave()
    }
    println("Encode successfully")
}