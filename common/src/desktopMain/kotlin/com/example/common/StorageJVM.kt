package com.example.common

import com.example.common.models.SaveContainer
import com.example.common.storage.Dir1
import com.example.common.storage.createFileSlot
import com.example.common.storage.theFileArray
import com.example.common.storage.writeToFile
import com.example.common.ui.main_screen.configurationOfSaldo
import com.example.common.ui.main_screen.stateFall
import com.example.common.utils.ListOfSlots
import com.example.common.utils.StateMachine.currentJSONObjectName
import com.example.common.utils.generateJsonFile
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

actual suspend fun saveNewBudgetJSON() {
    val json = Json.encodeToString<SaveContainer>(SaveContainer(configurationOfSaldo.value, stateFall))
    //val jsonConfig = Json.encodeToString<SaldoConfiguration>(configurationOfSaldo.value)

    println(">>>> ${json}")
    writeToFile(json, File(Dir1, currentJSONObjectName.fileName+".json"))
    //writeToFile(jsonConfig, File(Dir1,"config.json"))
}

actual suspend fun refreshBudgetJSON() {
    val json = Json.encodeToString<SaveContainer>(SaveContainer(configurationOfSaldo.value, stateFall))
    //val jsonConfig = Json.encodeToString<SaldoConfiguration>(configurationOfSaldo.value)

    println(">>>> ${json}")
    writeToFile(json, File(Dir1, currentJSONObjectName.fileName))
}

actual suspend fun decodeFromFile() {
    var json = ""
    try {
        BufferedReader(FileReader(File(Dir1, currentJSONObjectName.fileName+".json"))).use { br ->
            var line: String?
            while (br.readLine().also { line = it } != null) {
                json += line
                println(line)
            }
        }
    } catch (e: IOException) {
        println("ERROR ${e.message}")
    }

//    var jsonConfig = ""
//    try {
//        BufferedReader(FileReader(theFileConfig)).use { br ->
//            var line: String?
//            while (br.readLine().also { line = it } != null) {
//                jsonConfig += line
//                println(line)
//            }
//        }
//    } catch (e: IOException) {
//        println("ERROR ${e.message}")
//    }
    //println("<>> ${jsonConfig}")

    if (json.isNotEmpty()) {
        val container = Json.decodeFromString<SaveContainer>(json)
        stateFall = container.data

        configurationOfSaldo.value =  configurationOfSaldo.value.copy(
            investmentsAmount = container.config.investmentsAmount,
            investmentsName = container.config.investmentsName,
            startedDateMonth = container.config.startedDateMonth,
            startedDateYear = container.config.startedDateYear
        )
    } else {
       //encodeForSave()
    }
    println("Encode successfully config: ~ stateFall:${stateFall.size}")
}

actual suspend fun getListOfBudgets(): ArrayList<String> {
    return ArrayList(Dir1.listFiles().map { it.nameWithoutExtension })
}

actual fun createFiveSlots() {
    createFileSlot(ListOfSlots.FIRST().fileName+".json")
    createFileSlot(ListOfSlots.SECOND().fileName+".json")
    createFileSlot(ListOfSlots.THIRD().fileName+".json")
    createFileSlot(ListOfSlots.FOURTH().fileName+".json")
    createFileSlot(ListOfSlots.FIFTH().fileName+".json")
}