package com.example.common

import com.example.common.models.SaveContainer
import com.example.common.ui.main_screen.configurationOfSaldo
import com.example.common.ui.main_screen.stateFall
import com.example.common.utils.StateMachine
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

actual suspend fun saveNewBudgetJSON() {
    val json = Json.encodeToString<SaveContainer>(SaveContainer(config = configurationOfSaldo.value, stateFall))
    //val jsonConfig = Json.encodeToString<SaldoConfiguration>(configurationOfSaldo.value)

    println(">>>> ${json}")
    when(StateMachine.currentJSONObjectName.fileName) {
        "first_slot" -> PreferenceStorage.saveContainer1 = json
        "second_slot" -> PreferenceStorage.saveContainer1 = json
        "third_slot" -> PreferenceStorage.saveContainer1 = json
        "fourth_slot" -> PreferenceStorage.saveContainer1 = json
        "fifth_slot" -> PreferenceStorage.saveContainer1 = json
    }

    //PreferenceStorage.saveConfig = jsonConfig
    //writeToFile(json, File(Dir1,"data.json"))
}

actual fun createFiveSlots() {
    //
}

actual suspend fun refreshBudgetJSON() {
    val json = Json.encodeToString<SaveContainer>(SaveContainer(config = configurationOfSaldo.value, stateFall))
    //val jsonConfig = Json.encodeToString<SaldoConfiguration>(configurationOfSaldo.value)

    println(">>>> ${json}")
    PreferenceStorage.saveContainer1 = json
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
    var containerOfSavedSaldos: SaveContainer? = null

    try {
        when(StateMachine.currentJSONObjectName.fileName) {
            "first_slot" -> containerOfSavedSaldos =  Json.decodeFromString<SaveContainer>(PreferenceStorage.saveContainer1)
            "second_slot" ->   containerOfSavedSaldos = Json.decodeFromString<SaveContainer>(PreferenceStorage.saveContainer2)
            "third_slot" ->  containerOfSavedSaldos =  Json.decodeFromString<SaveContainer>(PreferenceStorage.saveContainer3)
            "fourth_slot" ->  containerOfSavedSaldos =  Json.decodeFromString<SaveContainer>(PreferenceStorage.saveContainer4)
            "fifth_slot" ->  containerOfSavedSaldos =  Json.decodeFromString<SaveContainer>(PreferenceStorage.saveContainer5)
        }
    }catch (e: Exception) {

    }


    // FIXME:
    if (containerOfSavedSaldos != null && containerOfSavedSaldos?.config?.investmentsAmount != null ) {
        //var containerOfSavedSaldos = Json.decodeFromString<SaveContainer>(PreferenceStorage.saveContainer1)

        stateFall = containerOfSavedSaldos.data
        val amount = containerOfSavedSaldos?.config?.investmentsAmount
        val name = containerOfSavedSaldos?.config?.investmentsName

        if (amount != null && name != null) {
            configurationOfSaldo.value =  configurationOfSaldo.value.copy(investmentsAmount = amount, investmentsName = name)
        }

    } else {
       // saveNewBudgetJSON()
    }

    println("Encode successfully")
}

actual suspend fun getListOfBudgets(): ArrayList<String> {
    return arrayListOf()
}