package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun forecastGhostMonth(
    //fs: FutureSaldo
    index: Int
) {
    var futureSaldo = remember { futureFall }
    Card(
        modifier = Modifier
            .width(150.dp)
            .padding(5.dp),
        elevation = 10.dp
    ) {
        Box(Modifier.fillMaxSize().clickable {  }) {
            Text("${futureSaldo.value?.sum1} ${0}", modifier = Modifier.fillMaxSize().padding(top = (1).dp,start = 0.dp).align(
                Alignment.TopCenter),
                fontFamily = FontFamily.Default, fontSize = 10.sp, fontWeight = FontWeight.Light,
                color = Color.LightGray
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
                                Text(modifier = Modifier.fillMaxWidth(), text = ">${item}")
                            }
                        )
                        // circle "plus" for add new stroke of Saldo
//                        item {
//                            plusik(isIncome = true, parentIndex = parentIndex)
//                        }
                    }
                }

                // SUMMA:
                Column(Modifier.weight(1f).background(Color.White), verticalArrangement = Arrangement.Center) {
                    Text("${futureSaldo.value?.income}", modifier = Modifier.padding(vertical = 2.dp),
                        fontFamily = FontFamily.Default, fontSize = 15.sp, fontWeight = FontWeight.Bold,textAlign = TextAlign.Center,
                        color = Color.Green
                    )
                    Text("${when(index) {
                        1 -> futureSaldo.value?.sum1
                        2 -> futureSaldo.value?.sum2
                        3 -> futureSaldo.value?.sum3
                        else -> futureSaldo.value?.sum3
                    }}", modifier = Modifier.padding(vertical = 5.dp).clickable {},
                        fontFamily = FontFamily.Default, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold,textAlign = TextAlign.Center,
                        color = Color.Blue
                    )
                    Text("${futureSaldo.value?.expense}", modifier = Modifier.padding(vertical = 2.dp),
                        fontFamily = FontFamily.Default, fontSize = 15.sp, fontWeight = FontWeight.Bold,textAlign = TextAlign.Center,
                        color = Color.Red
                    )
                }

                Row(
                    Modifier.weight(3f).background(Color.Red)
                ) {
                    LazyColumn {
                        itemsIndexed(futureSaldo.value?.expenses?: listOf(), itemContent = { index, item ->
                            Text(modifier = Modifier.fillMaxWidth(), text = ">${item}")
                            //strokeAgregator(itemStroke, parentIndex, index, isIncome = true)

                        })
                        // circle "plus" for add new stroke of Saldo
//                        item {
//                            plusik(isIncome = false, parentIndex)
//                        }
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