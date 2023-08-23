package com.example.common

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

actual suspend fun encodeForSave() {
    val json = Json.encodeToString<SaveContainer>(SaveContainer(stateFall))
    val jsonConfig = Json.encodeToString<SaldoConfiguration>(configurationOfSaldo.value)

    println(">>>> ${json}")
    PreferenceStorage.saveContainer = json
    PreferenceStorage.saveConfig = jsonConfig
    //writeToFile(json, File(Dir1,"data.json"))
}

actual suspend fun decodeFromFile() {

//    configurationOfSaldo.value = Json.decodeFromString<SaldoConfiguration>(PreferenceStorage.saveConfig)
//    stateFall = Json.decodeFromString<SaveContainer>(PreferenceStorage.saveContainer).data
//
    if (PreferenceStorage.saveConfig.isNotEmpty()) {
        val decodedConfig = Json.decodeFromString<SaldoConfiguration>(PreferenceStorage.saveConfig)
        configurationOfSaldo.value =  configurationOfSaldo.value.copy(investmentsAmount = decodedConfig.investmentsAmount, investmentsName = decodedConfig.investmentsName)
    }else {
        encodeForSave()
    }

    if (PreferenceStorage.saveContainer.isNotEmpty()) {

        stateFall = Json.decodeFromString<SaveContainer>(PreferenceStorage.saveContainer).data
    } else {
        encodeForSave()
    }



    println("Encode successfully")
}