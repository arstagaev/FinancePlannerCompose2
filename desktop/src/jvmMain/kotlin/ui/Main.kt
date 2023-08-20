package ui

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.common.AppX2
import com.example.common.updateXXX




fun main() = application {

    Window(onCloseRequest = ::exitApplication) {

        //App()
        //Test()
        //Tester()
        //TesterThree()
        updateXXX()
        AppX2()
    }
}

