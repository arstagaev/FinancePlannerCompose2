package ui

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.common.ui.main_screen.MainDashboard
import com.example.common.encodeForSave
import com.example.common.ui.main_screen.initital
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


fun main() = application {

    Window(title = "TFinance & Salary Planner", onCloseRequest = {
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
        MainDashboard()
    }
}

