package com.example.common.ui.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.colorGrayWindow2
import com.example.common.colorText
import com.example.common.custom.DateSelectionSection
import com.example.common.custom.defineNumMonth
import com.example.common.utils.toIntSafe

@Composable
fun EditorOfDate() {
    Column(
        Modifier.fillMaxWidth()//.height(50.dp)
            .background(colorGrayWindow2).clickable {
//                    isEditMode.value = false
//                    updateWhole()
            }, verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(modifier = Modifier.width(300.dp), text = "Choose started date:", color = colorText, fontSize = 30.sp)

        Spacer(modifier = Modifier.height(30.dp))

        DateSelectionSection(
            onYearChosen = {
                //chosenYear.value = it.toIntSafe()

                configurationOfSaldo.value.startedDateYear = it.toIntSafe()
//                            println("year ${it}  ${year}")
            },
            onMonthChosen = {
                //chosenMonth.value = monthsNames.map { it.name }.indexOf(it)
                configurationOfSaldo.value.startedDateMonth  = defineNumMonth(it)
            }
        )

        Spacer(modifier = Modifier.fillMaxWidth().height(30.dp))

        Button(
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            onClick = {
                updateWhole()
                inputDateMode.value = false
            }
        ) {
            Text(
                text = "Done",
                style = MaterialTheme.typography.button,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

//                    Box(Modifier.width(150.dp).fillMaxHeight().padding(5.dp)) {
//                        Text("Save init date", color = colorTextCreditTitle, fontSize = 30.sp)
//                    }


    }
}