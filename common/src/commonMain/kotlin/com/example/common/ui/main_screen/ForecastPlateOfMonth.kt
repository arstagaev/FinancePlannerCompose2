package com.example.common.ui.main_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.enums.SaldoMode
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.plus

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun forecastGhostMonth(
    index: Int
) {
    var futureSaldo = remember { futureFall }
    var dt = remember { mutableStateOf("") }
    var saldoModeInternal = remember { saldoMode }

    LaunchedEffect(futureSaldo.value) {
        var dateCustom = futureSaldo.value?.startForecastDate?.plus(DatePeriod(months = index))
        dt.value = "${dateCustom?.year} ${dateCustom?.month}"
    }
    Card(
        modifier = Modifier
            .width(150.dp)
            .padding(5.dp),
        elevation = 10.dp
    ) {

        if (saldoModeInternal.value == SaldoMode.SETUP_SETTINGS || saldoModeInternal.value == SaldoMode.LOADING) {

            Box(Modifier.fillMaxSize().shimmerEffect())
            return@Card
        }

        Box(Modifier.fillMaxSize().clickable {  }) {
            Text("${dt?.value}", modifier = Modifier.fillMaxSize().padding(top = (1).dp,start = 10.dp).align(
                Alignment.TopCenter),
                fontFamily = FontFamily.Default, fontSize = 10.sp, fontWeight = FontWeight.Light,
                color = Color.DarkGray
            )

            Column(
                modifier = Modifier.fillMaxSize().padding(top = 15.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    Modifier.weight(3f).background(Color.Green))
                {
                    LazyColumn {
                        itemsIndexed(
                            futureSaldo.value?.incomes ?: listOf(),
                            itemContent = { index, item ->
                                //strokeAgregator(item, parentIndex, index)
                                //Text(modifier = Modifier.fillMaxWidth(), text = ">${item}")
                                strokeFutureSaldo(item)
                            }
                        )
                    }
                }
                // SUMMA:
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.SpaceBetween) {
                    Row(modifier = Modifier.fillMaxWidth().background(colorDebitResult)) {
                        Text("${futureSaldo.value?.income}", modifier = Modifier.padding(vertical = 2.dp),
                            fontFamily = FontFamily.Default, fontSize = 10.sp, fontWeight = FontWeight.Bold,textAlign = TextAlign.Center,
                            //color = Color.Green
                        )
                    }

                    Text("${when(index) {
                        1 -> futureSaldo.value?.sum1
                        2 -> futureSaldo.value?.sum2
                        3 -> futureSaldo.value?.sum3
                        else -> futureSaldo.value?.sum3
                    }}", modifier = Modifier.basicMarquee(iterations = 10).padding(vertical = 5.dp).clickable {
                        GlobalScope.async {
                            updateWhole()
                        }
                    },
                        fontFamily = FontFamily.Default, fontSize = 25.sp, fontWeight = FontWeight.ExtraBold,textAlign = TextAlign.Center,
                        color = Color.DarkGray
                    )
                    Row(modifier = Modifier.fillMaxWidth().background(colorCreditResult)) {
                        Text(
                            "${futureSaldo.value?.expense}",
                            modifier = Modifier.padding(vertical = 2.dp),
                            fontFamily = FontFamily.Default,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            //color = Color.Red
                        )
                    }
                }
                Row(
                    Modifier.weight(3f).fillMaxSize().background(Color.Red)
                ) {
                    LazyColumn {
                        itemsIndexed(futureSaldo.value?.expenses?: listOf(), itemContent = { index, item ->
                            strokeFutureSaldo(item)
                        })
                    }
                }
            }

            Box(
                Modifier.fillMaxSize()
                .background(color = Color.LightGray.copy(alpha = 0.9f))
            )
        }
    }
}

@Composable
fun strokeFutureSaldo(amount: Int) {
    Card (Modifier.fillMaxWidth().padding(1.dp),
        shape = RoundedCornerShape(5.dp),
        elevation = 10.dp) {

        Column(
            Modifier.fillMaxSize()//.width(100.dp)
                .background(Color.Transparent)
        ) {
            Text(modifier = Modifier.padding(start = 5.dp), text ="${amount} \uD83D\uDD04")
        }
    }
}