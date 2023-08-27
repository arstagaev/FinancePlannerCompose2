package com.example.common.ui.main_screen

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.common.models.SaldoCell
import com.example.common.utils.currency
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.ArrayList

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlateOfMonth(parentIndex: Int, parentItem: ArrayList<SaldoCell>) {
    val res = resultFall.collectAsState(resultArray)
    var saldoModeInternal = remember { saldoMode }
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
            val dt = if (res.value.size > parentIndex) res.value[parentIndex].date else null
            Text("${dt?.year} ${dt?.month} ", modifier = Modifier.fillMaxWidth().padding(top = (1).dp,start = 10.dp).align(
                Alignment.TopCenter)
//                .clickable {
//                    inputDateMode.value = !inputDateMode.value
//                }
                ,
                fontFamily = FontFamily.Default, fontSize = 10.sp, fontWeight = FontWeight.Light,
                color = fontTitleMonth//Color.DarkGray
            )

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
                            parentItem.filter { it.amount > 0 }.sortedByDescending { it.amount },
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
                        Text("Σ Income:"+"${if (res.value.size > parentIndex) res.value[parentIndex].income else 0}", modifier = Modifier.padding(vertical = 2.dp),
                            fontFamily = FontFamily.Default, fontSize = 10.sp, fontWeight = FontWeight.Bold,textAlign = TextAlign.Center,
                            color = colorTextDebitTitle
                        )
                    }

                    Text("${ if (res.value.size > parentIndex) res.value[parentIndex].sum else 0}".currency(), modifier = Modifier.basicMarquee(iterations = 10).padding(vertical = 5.dp).clickable {
                        GlobalScope.async {
                            updateWhole()
                        }
                    },
                        fontFamily = FontFamily.Default, fontSize = 25.sp, fontWeight = FontWeight.ExtraBold,textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis,
                        color = colorTextSumMonth //Color.DarkGray
                    )
                    Row(modifier = Modifier.fillMaxWidth().background(
                        colorCreditResult
                    )) {
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

                Column(
                    Modifier.weight(3f)
                    //.background(colorCredit)
                ) {
                    Spacer(Modifier.fillMaxWidth().height(3.dp))
                    LazyColumn {
                        itemsIndexed(parentItem.filter { it.amount < 0 }.sortedByDescending { it.amount }, itemContent = { index, itemStroke ->
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