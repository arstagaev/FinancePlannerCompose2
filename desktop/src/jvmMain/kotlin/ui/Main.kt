package ui

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.common.saveNewBudgetJSON
import com.example.common.ui.starter_screen.StarterScreen
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


fun main() = application {

    Window(title = "TFinance & Salary Planner", onCloseRequest = {
        exitApplication()
//        CoroutineScope(CoroutineName("maim_app")).launch {
//            //saveNewBudgetJSON()
//
//        }

    }) {

        //App()
        //Test()
        //Tester()
        //TesterThree()


        //MaterialTheme(colors = darkFinColors)
        //BudgetScreen()
        StarterScreen()
    }
}

