package com.example.common.ui.main_screen

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.colorCard
import com.example.common.colorTextSumMonth
import com.example.common.enums.SaldoMode
import com.example.common.fontTitleMonth
import com.example.common.getPlatformName
import com.example.common.models.MonthSaldo
import com.example.common.ui.main_dashboard.colorCreditResult
import com.example.common.ui.main_dashboard.colorDebitResult
import com.example.common.ui.main_dashboard.colorTextCreditTitle
import com.example.common.ui.main_dashboard.colorTextDebitTitle
import com.example.common.ui.main_dashboard.resultArray
import com.example.common.ui.main_dashboard.resultFall
import com.example.common.ui.main_dashboard.saldoMode
import com.example.common.ui.main_dashboard.showTips
import com.example.common.ui.main_dashboard.uiConfig
import com.example.common.ui.main_dashboard.updateWhole
import com.example.common.utils.Platform
import com.example.common.utils.currency
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlateOfMonth(parentIndex: Int, parentItem: MonthSaldo) {
    val res = resultFall.collectAsState(resultArray)
    var saldoModeInternal = remember { saldoMode }
    val dt = if (res.value.size > parentIndex) res.value[parentIndex].date else null
    val showTipsInternal = remember { showTips }
    var config = remember { uiConfig }

    Card(
        modifier = Modifier
            .width(150.dp)
            .padding(5.dp),
        shape = RoundedCornerShape(10.dp)
        //elevation = 10.dp
    ) {
        if (saldoModeInternal.value == SaldoMode.SETUP_SETTINGS || saldoModeInternal.value == SaldoMode.LOADING) {

            Box(Modifier.fillMaxSize().shimmerEffect())
            return@Card
        }
        Box(
            Modifier.fillMaxSize().background(
            colorCard
            //Color.LightGray
        )) {
            Row(modifier = Modifier.fillMaxWidth().padding(top = (1).dp,start = 10.dp).align(
                Alignment.TopCenter), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                Text("${dt?.year} ${dt?.month} ",
//                .clickable {
//                    inputDateMode.value = !inputDateMode.value
//                }
                    //,
                    fontFamily = FontFamily.Default, fontSize = 10.sp, fontWeight = FontWeight.Light,
                    color = fontTitleMonth//Color.DarkGray
                )
                if (config.value.currentMonthInList == parentIndex) {
//                    val infiniteTransition = rememberInfiniteTransition()
//                    val bgColor by infiniteTransition.animateColor(
//                        initialValue = Color.Red,
//                        targetValue = Color.Green,
//                        animationSpec = infiniteRepeatable(
//                            animation = tween(1000, easing = LinearEasing),
//                            repeatMode = RepeatMode.Reverse
//                        )
//                    )
                    //
                    val infiniteTransition = rememberInfiniteTransition()
                    val animatedColor by infiniteTransition.animateColor(
                        initialValue = Color(0xFF60DDAD),
                        targetValue = Color(0xFF4285F4),
                        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse)//,
                        //label = "color"
                    )
                    Box(
                        modifier = Modifier.size(8.dp).clip(CircleShape).background(animatedColor)
                    )//.align(Alignment.TopEnd).padding(top = 6.dp, end = 10.dp))
                }
            }


            Column(
                modifier = Modifier.fillMaxSize().padding(top = 15.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Column(
                    Modifier.weight(3f)
                    //.background(colorDebit)
                )
                {
                    Spacer(Modifier.fillMaxWidth().height(3.dp))
                    LazyColumn {
                        itemsIndexed(
                            parentItem.incomes.sortedByDescending { it.amount }
                            ,
                            itemContent = { index, item ->
                                strokeAgregator(item, parentIndex, index)
                                //Text(">${item}")
                            }
                        )
                        // circle "plus" for add new stroke of Saldo
                        item {
                            plusik(isIncome = true, parentIndex = parentIndex)
                        }
                    }
                }

                // SUMMA:
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                    Row(modifier = Modifier.fillMaxWidth().background(
                        colorDebitResult
                    )) {
                        if (showTipsInternal.value) {
                            Text("Cumulative debit", modifier = Modifier.shimmerEffectBlue().padding(vertical = 2.dp).basicMarquee(iterations = 10),
                                fontFamily = FontFamily.Default, fontSize = 10.sp, fontWeight = FontWeight.Bold,textAlign = TextAlign.Center,
                                color = Color.Black
                            )
                        } else {
                            Text("Σ Income:"+"${if (res.value.size > parentIndex) res.value[parentIndex].income else 0}", modifier = Modifier.padding(vertical = 2.dp),
                                fontFamily = FontFamily.Default, fontSize = 10.sp, fontWeight = FontWeight.Bold,textAlign = TextAlign.Center,
                                color = colorTextDebitTitle
                            )
                        }

                    }
                    if (showTipsInternal.value) {
                        Text("Total sum on hands, balance", modifier = Modifier.shimmerEffectBlue().padding(vertical = 2.dp).basicMarquee(iterations = 10),
                            fontFamily = FontFamily.Default, fontSize = 20.sp, fontWeight = FontWeight.Bold,textAlign = TextAlign.Center,
                            color = Color.Black
                        )
                    } else {
                        Text(
                            "${if (res.value.size > parentIndex) res.value[parentIndex].sum else 0}".currency(),
                            modifier = Modifier.basicMarquee(iterations = 10)
                                .padding(vertical = 5.dp).clickable {
                                GlobalScope.async {
                                    updateWhole()
                                }
                            },
                            fontFamily = FontFamily.Default,
                            fontSize = if (getPlatformName() == Platform.DESKTOP) 25.sp else 30.sp,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center,
                            overflow = TextOverflow.Ellipsis,
                            color = colorTextSumMonth //Color.DarkGray
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth().background(
                        colorCreditResult
                    )) {
                        if (showTipsInternal.value) {
                            Text("Cumulative credit", modifier = Modifier.shimmerEffectBlue().padding(vertical = 2.dp).basicMarquee(iterations = 10),
                                fontFamily = FontFamily.Default, fontSize = 10.sp, fontWeight = FontWeight.Bold,textAlign = TextAlign.Center,
                                color = Color.Black
                            )
                        } else {
                            Text(
                                "Σ Expense: ${if (res.value.size > parentIndex) res.value[parentIndex].expense else 0}",
                                modifier = Modifier.padding(vertical = 2.dp),
                                fontFamily = FontFamily.Default,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = colorTextCreditTitle
                            )
                        }
                    }
                }

                Column(
                    Modifier.weight(3f)
                    //.background(colorCredit)
                ) {
                    Spacer(Modifier.fillMaxWidth().height(3.dp))
                    LazyColumn {
                        itemsIndexed(
                            parentItem.expenses.sortedByDescending { it.amount }
                            , itemContent = { index, itemStroke ->
                            //Text(">${item}")
                            strokeAgregator(itemStroke, parentIndex, index, isIncome = false)

                        })
                        // circle "plus" for add new stroke of Saldo
                        item {
                            plusik(isIncome = false, parentIndex)
                        }
                    }

                }
            }
        }
    }
}