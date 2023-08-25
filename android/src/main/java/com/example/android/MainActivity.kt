package com.example.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import com.example.common.ui.mainscreen.AppX2
import com.example.common.encodeForSave
import com.example.common.ui.mainscreen.initital
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    var crt = CoroutineScope(CoroutineName("Activity"))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initital()
        setContent {
            MaterialTheme {
                //App()
                AppX2()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        crt.launch {
            encodeForSave()
        }

    }

    override fun onStop() {
        super.onStop()
        crt.cancel()
    }
}
