package com.example.common

import java.io.*
import javax.swing.JFileChooser

private val MAINFOLDER = "TFinance"
val Dir1 = File("${JFileChooser().fileSystemView.defaultDirectory}/$MAINFOLDER")
val theFileArray = File(
    Dir1,"data.json"
)
val theFileConfig = File(
    Dir1,"config.json"
)
fun createDemoConfigFile() : File {
    println("createDemoConfigFile ${Dir1.absoluteFile}")

    Dir1.mkdir()
    if (!theFileArray.exists()) {
        theFileArray.createNewFile()
        println("Excel file-config created: ${theFileArray.absoluteFile}")
    }
    return theFileArray
}


fun writeToFile(msg: String, fl: File) {
    createDemoConfigFile()
    println(">>> write file")
    //IF first launch
    //val fl = Dir4MainConfig_Log
    if (!fl.exists()) {
        fl.createNewFile()
    }
    fl.bufferedWriter().use { out ->
        out.write(msg)
    }
}

expect suspend fun encodeForSave()

expect suspend fun decodeFromFile()
