package com.example.common

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

actual fun encodeForSave() {
    val json = Json.encodeToString<SaveContainer>(SaveContainer(stateFall))
    println(">>>> ${json}")
    PreferenceStorage.saveContainer = json
    //writeToFile(json, File(Dir1,"data.json"))
}

actual fun decodeFromFile() {

    stateFall = Json.decodeFromString<SaveContainer>(PreferenceStorage.saveContainer).data
    println("Encode successfully")
}