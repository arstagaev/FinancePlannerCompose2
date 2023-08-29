package com.example.common.ui.list_saldos_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.colorCard
import com.example.common.colorGrayWindow2
import com.example.common.colorTextSumMonth
import com.example.common.createFiveSlots
import com.example.common.ui.starter_screen.showBudget
import com.example.common.utils.ListOfSlots
import com.example.common.utils.StateMachine.currentJSONObjectName

@Composable
fun ListOfBudgets() {
    val listOfBudgets = mutableStateListOf<ListOfSlots>(
        ListOfSlots.FIRST(),
        ListOfSlots.SECOND(),
        ListOfSlots.THIRD(),
        ListOfSlots.FOURTH(),
        ListOfSlots.FIFTH()
    )

    LaunchedEffect(true) {
        createFiveSlots()
//        Dir1.listFiles().forEachIndexed { index, file ->
//            listOfBudgets.add(file.name)
//        }
    }

    Column(
        Modifier.fillMaxSize().background(colorGrayWindow2)
    ) {
        Text(modifier = Modifier.fillMaxWidth().padding(start = 20.dp, top = 20.dp), text = "Chose budget:", color = colorTextSumMonth, fontSize = 30.sp)
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(listOfBudgets) {index: Int, item: ListOfSlots ->
                Card (
                    Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
                    ,
                    shape = RoundedCornerShape(5.dp),
                    elevation = 10.dp) {

                    Column(
                        Modifier.fillMaxSize()//.width(100.dp)
                            .background(colorCard).clickable {
                                currentJSONObjectName = item
                                showBudget.value = true
                            }
                    ) {
                        Text(modifier = Modifier.fillMaxWidth().padding(start =10.dp ), text = item.fileName, color = colorTextSumMonth, fontSize = 30.sp)

                    }
                }
            }
        }
    }
}