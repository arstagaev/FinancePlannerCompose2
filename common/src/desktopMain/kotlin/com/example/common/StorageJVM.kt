package com.example.common

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

actual fun encodeForSave() {
    val json = Json.encodeToString<SaveContainer>(SaveContainer(stateFall))
    println(">>>> ${json}")
    writeToFile(json, File(Dir1,"data.json"))
}

actual fun decodeFromFile() {
    //val file = File()
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

    stateFall = Json.decodeFromString<SaveContainer>(json).data
    println("Encode successfully")
}