package com.example.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.defaultComponentContext
import com.example.common.ui.root.RootComponent
import com.example.common.ui.root.RootContent
import com.example.common.ui.starter_screen.StarterScreen
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

class MainActivity : AppCompatActivity() {
    var crt = CoroutineScope(CoroutineName("Activity"))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Always create the root component outside Compose on the main thread
        val root = RootComponent(componentContext = defaultComponentContext())

        setContent {
            MaterialTheme {
                Surface {
                    RootContent(component = root, modifier = Modifier.fillMaxSize())
                }
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
