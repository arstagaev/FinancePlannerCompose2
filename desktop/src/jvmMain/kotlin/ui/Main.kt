package ui

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.common.ui.mainscreen.AppX2
import com.example.common.encodeForSave
import com.example.common.ui.mainscreen.initital
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


fun main() = application {

    Window(onCloseRequest = {
        CoroutineScope(CoroutineName("maim_app")).launch {
            encodeForSave()
            exitApplication()
        }

    }) {

        //App()
        //Test()
        //Tester()
        //TesterThree()
        initital()

        //MaterialTheme(colors = darkFinColors)
        AppX2()
    }
}

