package com.example.common.ui.main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.colorCard
import com.example.common.enums.SaldoMode
import com.example.common.ui.main_dashboard.futureFall
import com.example.common.ui.main_dashboard.saldoMode
import com.example.common.utils.currency

@Composable
fun longForecast() {
    var futureSaldo = remember { futureFall }
    val text1 = buildAnnotatedString {
        withStyle(SpanStyle(color = Color.White)) {
            append("After half of year:")
        }
        withStyle(SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) {
            append("\n" + "${futureSaldo.value?.periodHalfYear}".currency())
        }
    }
    val text2 = buildAnnotatedString {
        withStyle(SpanStyle(color = Color.White)) {
            append("After 1 year:")
        }
        withStyle(SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) {
            append("\n" + "${futureSaldo.value?.periodFirstYear}".currency())
        }
    }
    val text3 = buildAnnotatedString {
        withStyle(SpanStyle(color = Color.White)) {
            append("After 2 years:")
        }
        withStyle(SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) {
            append("\n" + "${futureSaldo.value?.periodSecondYear}".currency())
        }
    }
    var saldoModeInternal = remember { saldoMode }

    Column(Modifier.fillMaxHeight().padding(end = 50.dp), verticalArrangement = Arrangement.SpaceBetween) {

        Card(
            modifier = Modifier
                .width(150.dp)
                .height(100.dp)
                .padding(5.dp),
            elevation = 10.dp
        ) {
            if (saldoModeInternal.value == SaldoMode.SETUP_SETTINGS || saldoModeInternal.value == SaldoMode.LOADING) {

                Box(Modifier.fillMaxSize().shimmerEffect())
                return@Card
            }
            Box(Modifier.fillMaxSize().background(colorCard)) {
                Text(text1,
                    modifier = Modifier.padding(4.dp).align(Alignment.Center)
                        .clickable {},
                    fontSize = 20.sp, fontFamily = FontFamily.Default,
                    //color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }

        }
        Card(
            modifier = Modifier
                .width(150.dp)
                .height(100.dp)
                .padding(5.dp),
            elevation = 10.dp
        ) {
            if (saldoModeInternal.value == SaldoMode.SETUP_SETTINGS || saldoModeInternal.value == SaldoMode.LOADING) {

                Box(Modifier.fillMaxSize().shimmerEffect())
                return@Card
            }
            Box(Modifier.fillMaxSize().background(colorCard)) {
                Text(text2,
                    modifier = Modifier.padding(4.dp).align(Alignment.Center)
                        .clickable {},
                    fontSize = 20.sp, fontFamily = FontFamily.Default,
                    color = Color.Black, textAlign = TextAlign.Center
                )
            }

        }
        Card(
            modifier = Modifier
                .width(150.dp)
                .height(100.dp)
                .padding(5.dp),
            elevation = 10.dp
        ) {
            if (saldoModeInternal.value == SaldoMode.SETUP_SETTINGS || saldoModeInternal.value == SaldoMode.LOADING) {

                Box(Modifier.fillMaxSize().shimmerEffect())
                return@Card
            }
            Box(Modifier.fillMaxSize().background(colorCard)) {
                Text(text3,
                    modifier = Modifier.padding(4.dp).align(Alignment.Center)
                        .clickable {},
                    fontSize = 20.sp, fontFamily = FontFamily.Default,
                    color = Color.Black, textAlign = TextAlign.Center
                )
            }
        }
    }
}
