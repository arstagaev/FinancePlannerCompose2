package ui

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.common.AppX2
import com.example.common.initital
import com.example.common.updateWhole




fun main() = application {

    Window(onCloseRequest = ::exitApplication) {

        //App()
        //Test()
        //Tester()
        //TesterThree()
        initital()
        AppX2()
    }
}

