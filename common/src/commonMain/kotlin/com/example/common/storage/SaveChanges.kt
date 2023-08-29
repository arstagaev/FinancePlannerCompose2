package com.example.common.storage

import com.example.common.utils.StateMachine.currentJSONObjectName
import java.io.*
import javax.swing.JFileChooser

private val MAINFOLDER = "TFinance"
val Dir1 = File("${JFileChooser().fileSystemView.defaultDirectory}/$MAINFOLDER")
val theFileArray = File(
    Dir1, "${currentJSONObjectName}.json"
)

//val theFileConfig = File(
//    Dir1,"config.json"
//)

fun createFileSlot(fileName: String) : File {
    println("createDemoConfigFile ${Dir1.absoluteFile}")

    Dir1.mkdir()
    val file = File(Dir1,fileName)
    if (!file.exists()) {
        file.createNewFile()
        println("Excel file-config created: ${file.absoluteFile}")
    }
    return file
}

fun createFileSlot(file: File) : File {
    println("createDemoConfigFile ${Dir1.absoluteFile}")

    Dir1.mkdir()
    //val file = File(Dir1,fileName)
    if (!file.exists()) {
        file.createNewFile()
        println("Excel file-config created: ${file.absoluteFile}")
    }
    return file
}


fun writeToFile(msg: String, fl: File) {
    createFileSlot(fl)
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

fun getListOfBudgets() {
    val listFiles = Dir1.listFiles()
}
