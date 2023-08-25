package com.example.common

import com.example.common.ui.mainscreen.SaldoConfiguration
import com.example.common.ui.mainscreen.SaveContainer
import com.example.common.ui.mainscreen.configurationOfSaldo
import com.example.common.ui.mainscreen.stateFall
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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