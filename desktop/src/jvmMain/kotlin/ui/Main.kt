package ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.example.common.saveNewBudgetJSON
import com.example.common.ui.root.RootComponent
import com.example.common.ui.root.RootContent
import com.example.common.ui.starter_screen.StarterScreen
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.arkivanov.essenty.lifecycle.LifecycleRegistry

fun main() = application {
    val root = RootComponent(componentContext = DefaultComponentContext(lifecycle = LifecycleRegistry()))
    Window(title = "TFinance & Salary Planner", onCloseRequest = {
        exitApplication()
//        CoroutineScope(CoroutineName("maim_app")).launch {
//            //saveNewBudgetJSON()
//
//        }

    }) {
        RootContent(component = root, modifier = Modifier.fillMaxSize())
    }
}

