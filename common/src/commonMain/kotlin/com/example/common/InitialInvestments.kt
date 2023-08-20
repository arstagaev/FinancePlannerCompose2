package com.example.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InitialInvestments() {
    var configSaldos = remember { configurationOfSaldo }
    var isEditLocal = remember { mutableStateOf(false) }

    var saldoStrokeName = remember { mutableStateOf("${configSaldos.value.investmentsName}") }
    var saldoStrokeAmount = remember { mutableStateOf("${configSaldos.value.investmentsAmount}") }


    LaunchedEffect(isEditMode.value) {
        if (!isEditMode.value) {
            if (isEditLocal.value) {
                updateXXX()
                configurationOfSaldo.value = SaldoConfiguration(investmentsName = saldoStrokeName.value, investmentsAmount = saldoStrokeAmount.value.toInt())
                //updateStroke(oldSaldo = saldoCell, newSaldoCell = SaldoCell(amount = saldoStrokeAmount.value.toInt(), name = saldoStrokeName.value, saldoCell.isConst), parentIndex,)
                isEditLocal.value = false
            }
        }
    }

    //var futureSaldo = remember { futureFall }
    val textInvest = buildAnnotatedString {
        withStyle(SpanStyle(color = Color.Blue)) {
            append("${saldoStrokeName.value}")
        }
        withStyle(SpanStyle(
            color = Color.DarkGray, fontSize = 25.sp,
            fontWeight = FontWeight.ExtraBold, fontFamily = FontFamily.Default)) {
            append("\n" + "${saldoStrokeAmount.value}")
        }
    }

    Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
        Card(
            modifier = Modifier
                .width(150.dp)
                //.height(100.dp)
                .padding(5.dp),
            elevation = 10.dp
        ) {
            Column(Modifier.fillMaxWidth()
                .background(Color.LightGray).clickable {
                    isEditLocal.value = true
                    isEditMode.value = true
                }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                if (isEditLocal.value) {
                    TextField(
                        modifier = Modifier.fillMaxWidth()//.height(40.dp)
                            .background(Color.White),
                        value = saldoStrokeAmount.value.toString(),
                        onValueChange = {
                            val newNum = it.filter { it.isDigit() }
                            if (newNum.isNotEmpty()) {
                                saldoStrokeAmount.value = newNum
                            }
                            //println("->>${currentBudgetX.value.joinToString()}")
                            //println("stroke ${saldoStrokeAmount.value} ${CalcModule2.currentBudgetX.value.joinToString()}")
                        },
                        textStyle = TextStyle.Default.copy(fontSize = 15.sp)
                    )
                    TextField(
                        modifier = Modifier.fillMaxWidth()//.height(40.dp)
                            .background(Color.Transparent),
                        value = saldoStrokeName.value?:"",

                        onValueChange = {
                            if (it.isNotEmpty()) {
                                saldoStrokeName.value = it
                                //newCellSaldo.value.name = it
                                //isEditByHuman.value = true
                            }
                        },
                        label = { Text("Enter name for source of amount", fontSize = 10.sp) },
                        textStyle = TextStyle.Default.copy(fontSize = 10.sp),
                    )
//                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
//                    Row(Modifier.clickable { deleteCell(monthIndex = parentIndex, saldoCell = saldoCell) }) {
//                        Text("❌", fontSize = 20.sp)
//                    }
//                    Row(Modifier.clickable { deleteCell(monthIndex = parentIndex, saldoCell = saldoCell, true) }) {
//                        Text("❌\uD83D\uDD1C", fontSize = 20.sp)
//                    }
//                }

                } else {
                    Text(
                        textInvest,
                        modifier = Modifier.padding(4.dp)//.align(Alignment.Center)
                            ,
                        fontSize = 10.sp, fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center
                    )
                }
            }



        }
    }

}