package com.example.common.ui.main_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.colorCard
import com.example.common.colorTextSecondary
import com.example.common.colorTextSumMonth
import com.example.common.enums.SaldoMode

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InitialInvestments() {
    //var configSaldos = remember { configurationOfSaldo }
    var isEditLocal = remember { mutableStateOf(false) }

    var saldoStrokeName = remember { mutableStateOf("${configurationOfSaldo.value.investmentsName}") }
    var saldoStrokeAmount = remember { mutableStateOf("${configurationOfSaldo.value.investmentsAmount}") }

    var saldoModeInternal = remember { saldoMode }

    LaunchedEffect(isEditMode.value, configurationOfSaldo.value) {
        println("TRIGGERED ${configurationOfSaldo.value.investmentsAmount}")


        if (!isEditMode.value) {
            if (isEditLocal.value) {
                //configurationOfSaldo.value = SaldoConfiguration(investmentsName = saldoStrokeName.value, investmentsAmount = saldoStrokeAmount.value.toInt())
                configurationOfSaldo.value = configurationOfSaldo.value.copy(investmentsName = saldoStrokeName.value, investmentsAmount = saldoStrokeAmount.value.toInt())
                updateWhole()
                isEditLocal.value = false
            }
        }
        saldoStrokeName.value = configurationOfSaldo.value.investmentsName.toString()
        saldoStrokeAmount.value = configurationOfSaldo.value.investmentsAmount.toString()
    }

    val textInvest = buildAnnotatedString {
        withStyle(SpanStyle(color = Color.DarkGray)) {
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
            elevation = 10.dp,
            shape = RoundedCornerShape(20.dp)
        ) {
            if (saldoModeInternal.value == SaldoMode.SETUP_SETTINGS || saldoModeInternal.value == SaldoMode.LOADING) {

                Box(Modifier.fillMaxSize().shimmerEffect())
                return@Card
            }
            Column(Modifier.fillMaxWidth()
                .background(colorCard).clickable {
                    isEditLocal.value = true
                    isEditMode.value = true
                }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                if (isEditLocal.value) {
                    TextField(
                        modifier = Modifier.fillMaxWidth()//.height(40.dp)
                            .background(Color.White),
                        value = saldoStrokeAmount.value.toString(),
                        onValueChange = {
                            //val newNum = it.filter { it.isDigit() }
                            if (it.isNotEmpty()) {
                                saldoStrokeAmount.value = it.replace("[^0-9\\-]".toRegex(), "")
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
                } else {
                    Text("${saldoStrokeAmount.value}", modifier = Modifier.basicMarquee(iterations = 10).padding(vertical = 5.dp)
                        //.clickable {}
                        ,
                        fontFamily = FontFamily.Default, fontSize = 25.sp, fontWeight = FontWeight.ExtraBold,textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis,
                        color = colorTextSumMonth //Color.DarkGray
                    )
//                    Text(
//                        "${saldoStrokeAmount.value}",
//                        modifier = Modifier.basicMarquee(iterations = 10).padding(4.dp)//.align(Alignment.Center)
//                            ,color = colorText,
//                        fontSize = 10.sp, fontFamily = FontFamily.Monospace,
//                        textAlign = TextAlign.Center
//                    )

                    Text(
                        "${saldoStrokeName.value}",
                        modifier = Modifier.basicMarquee(iterations = 10).padding(4.dp)//.align(Alignment.Center)
                        ,color = colorTextSecondary,
                        fontSize = 10.sp, fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}