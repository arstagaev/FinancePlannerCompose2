package com.example.common.ui.main_screen

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.enums.SaldoMode
import com.example.common.ui.main_screen.futureFall

@Composable
fun longForecast() {
    var futureSaldo = remember { futureFall }
    val text1 = buildAnnotatedString {
        withStyle(SpanStyle(color = Color.Blue)) {
            append("After half of year:")
        }
        append("\n" + "${futureSaldo.value?.periodHalfYear}")
    }
    val text2 = buildAnnotatedString {
        withStyle(SpanStyle(color = Color.Blue)) {
            append("After 1 year:")
        }
        append("\n" + "${futureSaldo.value?.periodFirstYear}")
    }
    val text3 = buildAnnotatedString {
        withStyle(SpanStyle(color = Color.Blue)) {
            append("After 2 years:")
        }
        append("\n" + "${futureSaldo.value?.periodSecondYear}")
    }
    var saldoModeInternal = remember { saldoMode }

    Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {

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
            Box(Modifier.fillMaxSize()) {
                Text(text1,
                    modifier = Modifier.padding(4.dp).align(Alignment.Center)
                        .clickable {},
                    fontSize = 20.sp, fontFamily = FontFamily.Monospace,
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
            Box(Modifier.fillMaxSize()) {
                Text(text2,
                    modifier = Modifier.padding(4.dp).align(Alignment.Center)
                        .clickable {},
                    fontSize = 20.sp, fontFamily = FontFamily.Monospace,
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
            Box(Modifier.fillMaxSize()) {
                Text(text3,
                    modifier = Modifier.padding(4.dp).align(Alignment.Center)
                        .clickable {},
                    fontSize = 20.sp, fontFamily = FontFamily.Monospace,
                    color = Color.Black, textAlign = TextAlign.Center
                )
            }
        }
    }
}
