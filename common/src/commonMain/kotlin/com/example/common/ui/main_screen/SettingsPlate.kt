package com.example.common.ui.main_screen

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.colorCard
import com.example.common.colorGrayWindow2
import com.example.common.colorText
import com.example.common.colorTextSecondary
import com.example.common.custom.DateSelectionSection
import com.example.common.custom.defineNumMonth
import com.example.common.enums.SaldoMode
import com.example.common.utils.toIntSafe

@Composable
fun EditorOfDate() {
    Column(
        Modifier.fillMaxWidth().height(250.dp)
            .background(colorGrayWindow2).clickable {
//                    isEditMode.value = false
//                    updateWhole()
            }, verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyRow(Modifier.fillMaxWidth()) {
            item {
                Card(modifier = Modifier//.width(100.dp)
                    .padding(10.dp), elevation = 10.dp, shape = RoundedCornerShape(20.dp)) {
                    Column(Modifier.fillMaxSize().background(colorCard)) {
                        Text(modifier = Modifier.width(300.dp).padding(start = 20.dp), text = "Choose started date:", color = colorText, fontSize = 20.sp)

                        Spacer(modifier = Modifier.height(30.dp))

                        DateSelectionSection(
                            onYearChosen = { configurationOfSaldo.value.startedDateYear = it.toIntSafe() },
                            onMonthChosen = { configurationOfSaldo.value.startedDateMonth  = defineNumMonth(it) }
                        )
                    }
                }
            }
            item {
                var currency = remember { mutableStateOf("${configurationOfSaldo.value.currentCurrency}") }
                var expanded by remember { mutableStateOf(false) }
                Card(modifier = Modifier.width(300.dp).height(70.dp)
                    .padding(10.dp)) {
                    var expanded by remember { mutableStateOf(false) }
                    val items = listOf("$", "€", "₽", "AED")
                    var selectedIndex by remember { mutableStateOf(0) }
                    Box(modifier = Modifier.fillMaxSize().background(
                        colorCard)
                    ) {
                        Text(modifier = Modifier.width(300.dp).padding(start = 20.dp).clickable {  expanded = true }, text = "Currency: ${items[selectedIndex]}", color = colorText, fontSize = 20.sp)

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.fillMaxWidth().background(
                                colorCard)
                        ) {
                            items.forEachIndexed { index, s ->
                                DropdownMenuItem(onClick = {
                                    selectedIndex = index
                                    configurationOfSaldo.value = configurationOfSaldo.value.copy(currentCurrency = s)
                                    expanded = false
                                }) {
                                    Text(text = s, color = colorTextSecondary, fontSize = 20.sp)
                                }
                            }
                        }
                    }

                }
            }
        }


        Spacer(modifier = Modifier.fillMaxWidth().height(30.dp))

        Button(
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            onClick = {
                updateWhole()
                if (saldoMode.value == SaldoMode.SHOW) {
                    saldoMode.value = SaldoMode.SETUP_SETTINGS
                } else {
                    saldoMode.value = SaldoMode.SHOW
                }
                //inputDateMode.value = false
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

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition()
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        )
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFB8B5B5),
                Color(0xFF8F8B8B),
                Color(0xFFB8B5B5),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}

fun Modifier.shimmerEffectBlue(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition()
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2000)
        )
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFBADEFB),
                Color(0xFF91CCFB),
                Color(0xFF44ACFE),
                Color(0xFF91CCFB),
                Color(0xFFBADEFB),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}