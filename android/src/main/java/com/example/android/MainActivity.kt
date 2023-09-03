package com.example.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import com.example.common.ui.starter_screen.StarterScreen
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

class MainActivity : AppCompatActivity() {
    var crt = CoroutineScope(CoroutineName("Activity"))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                //App()
                //BudgetScreen()
                StarterScreen()
            }
        }
    }

    override fun onPause() {
        super.onPause()
//        crt.launch {
//            encodeForSave()
//        }

    }

    override fun onStop() {
        super.onStop()
        crt.cancel()
    }
}
