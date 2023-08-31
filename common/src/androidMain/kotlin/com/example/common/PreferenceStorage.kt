package com.example.common

import android.content.Context
import android.content.SharedPreferences


object PreferenceStorage {

    private var NAME = "TFinance"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    fun clearAll() {
        preferences.edit {
            it.clear()
        }
    }

    /**
     * SharedPreferences extension function, so we won't need to call edit() and apply()
     * ourselves on every SharedPreferences operation.
     */
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var saveContainer1: String
        get() = preferences.getString("saveContainer1","").toString()
        set(value) = preferences.edit {
            it.putString("saveContainer1", value)
        }
    var saveContainer2: String
        get() = preferences.getString("saveContainer2","").toString()
        set(value) = preferences.edit {
            it.putString("saveContainer2", value)
        }
    var saveContainer3: String
        get() = preferences.getString("saveContainer3","").toString()
        set(value) = preferences.edit {
            it.putString("saveContainer3", value)
        }
    var saveContainer4: String
        get() = preferences.getString("saveContainer4","").toString()
        set(value) = preferences.edit {
            it.putString("saveContainer4", value)
        }
    var saveContainer5: String
        get() = preferences.getString("saveContainer5","").toString()
        set(value) = preferences.edit {
            it.putString("saveContainer5", value)
        }

    var saveConfig: String
        get() = preferences.getString("saveConfig","").toString()
        set(value) = preferences.edit {
            it.putString("saveConfig", value)
        }

//    var baseCurrency: String
//        get() = preferences.getString("baseCurrency","USD").toString()
//        set(value) = preferences.edit {
//            it.putString("baseCurrency", value)
//        }
//
//    var lastTimeOfRequestAvailableCurrency: String
//        get() = preferences.getString("lastTimeAvailableCurr_request", System.currentTimeMillis().toString() ).toString()
//        set(value) = preferences.edit {
//            it.putString("lastTimeAvailableCurr_request", value)
//        }

}