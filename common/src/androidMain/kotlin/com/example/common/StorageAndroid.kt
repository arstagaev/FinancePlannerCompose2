package com.example.common

import com.example.common.models.SaveContainer
import com.example.common.ui.main_screen.configurationOfSaldo
import com.example.common.ui.main_screen.stateFall
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

actual suspend fun saveNewBudgetJSON() {
    val json = Json.encodeToString<SaveContainer>(SaveContainer(config = configurationOfSaldo.value, stateFall))
    //val jsonConfig = Json.encodeToString<SaldoConfiguration>(configurationOfSaldo.value)

    println(">>>> ${json}")
    PreferenceStorage.saveContainer = json
    //PreferenceStorage.saveConfig = jsonConfig
    //writeToFile(json, File(Dir1,"data.json"))
}

actual fun createFiveSlots() {
    TODO()
}

actual suspend fun refreshBudgetJSON() {
    val json = Json.encodeToString<SaveContainer>(SaveContainer(config = configurationOfSaldo.value, stateFall))
    //val jsonConfig = Json.encodeToString<SaldoConfiguration>(configurationOfSaldo.value)

    println(">>>> ${json}")
    PreferenceStorage.saveContainer = json
}
actual suspend fun decodeFromFile() {

//    configurationOfSaldo.value = Json.decodeFromString<SaldoConfiguration>(PreferenceStorage.saveConfig)
//    stateFall = Json.decodeFromString<SaveContainer>(PreferenceStorage.saveContainer).data
//
//    if (PreferenceStorage.saveConfig.isNotEmpty()) {
//        val decodedConfig = Json.decodeFromString<SaldoConfiguration>(PreferenceStorage.saveConfig)
//        configurationOfSaldo.value =  configurationOfSaldo.value.copy(investmentsAmount = decodedConfig.investmentsAmount, investmentsName = decodedConfig.investmentsName)
//    }else {
//        encodeForSave()
//    }

    if (PreferenceStorage.saveContainer.isNotEmpty()) {
        val containerOfSavedSaldos = Json.decodeFromString<SaveContainer>(PreferenceStorage.saveContainer)

        stateFall = containerOfSavedSaldos.data
        configurationOfSaldo.value =  configurationOfSaldo.value.copy(investmentsAmount = containerOfSavedSaldos.config.investmentsAmount, investmentsName = containerOfSavedSaldos.config.investmentsName)

    } else {
        saveNewBudgetJSON()
    }



    println("Encode successfully")
}

actual suspend fun getListOfBudgets(): ArrayList<String> {
    return arrayListOf()
}