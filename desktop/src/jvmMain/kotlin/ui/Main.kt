package ui

import Consts._currentBudget
import Consts.currentBudget
import Consts.saldoState
import Saldo
import SaldoAction
import SaldoStroke
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import colorCredit
import colorDebit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import refresh

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {

        //val curBud = currentBudget.collectAsState()
        val ctx = CoroutineScope(Dispatchers.Default)
        val resd = remember { mutableStateListOf<Saldo>() }
        LaunchedEffect(true) {
            currentBudget.collect {r ->
                // fill whole budget by months
                r.forEach {
                    resd.add(it)
                }
            }
        }

        Column {
            val isEdit = remember { saldoState }
            // upper red line, which define we edit now texts
            AnimatedVisibility(
                saldoState.value.saldoAction == SaldoAction.EDITING
                //true
            ) {
                Row(Modifier.fillMaxWidth().height(50.dp).background(Color.Red).clickable {
                    saldoState.value = saldoState.value.copy(saldoAction = SaldoAction.SHOW)

                    val a = _currentBudget.value
                    val b = _currentBudget.value
                    _currentBudget.value = b

                }, horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
                ) {
//                    Box(Modifier.fillMaxSize().weight(1f).background(Color.Red).clickable {
//
//                    })
                    Text("Recalculate", fontSize = 30.sp)
                }
            }

            LazyRow(modifier = Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
                itemsIndexed(items = resd, itemContent = {indx, item ->
                    PlateMonth(item,indx)
                })
                // circle "plus" for add new month:
                item {
                    Box(modifier = Modifier.size(50.dp).clip(CircleShape).background(Color.White).clickable {

                    }) {
                        Text(text = "+", style = MaterialTheme.typography.body1,
                            modifier = Modifier.align(Alignment.Center), textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

    }
}

// whole Vertical plate, is symbol of month:
@Composable
fun PlateMonth(saldo: Saldo, indx: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        elevation = 10.dp
    ) {
        Box(Modifier.clickable {  }) {
            Column(
                modifier = Modifier.padding(top = 15.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(Modifier.weight(3f).background(colorDebit)) {
                    verticalList(saldo.wholeStrokes.filter { it.nature == Nature.DEBIT } as ArrayList<SaldoStroke>, saldo.month, saldo.year, indx, isDebet = true)
                }
                // SUMMA:
                Column(Modifier.weight(1f).background(Color.White), verticalArrangement = Arrangement.Center) {
                    Text("100 000", modifier = Modifier.padding(vertical = 2.dp),
                        fontFamily = FontFamily.Default, fontSize = 15.sp, fontWeight = FontWeight.Bold,textAlign = TextAlign.Center,
                        color = Color.Green
                    )
                    Text("100 000", modifier = Modifier.padding(vertical = 5.dp),
                        fontFamily = FontFamily.Default, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold,textAlign = TextAlign.Center,
                        color = Color.Blue
                    )
                    Text("100 000", modifier = Modifier.padding(vertical = 2.dp),
                        fontFamily = FontFamily.Default, fontSize = 15.sp, fontWeight = FontWeight.Bold,textAlign = TextAlign.Center,
                        color = Color.Red
                    )
                }
                Row(
                    Modifier.weight(3f).background(colorCredit)
                ) {
                    verticalList(saldo.wholeStrokes.filter { it.nature == Nature.CREDIT } as ArrayList<SaldoStroke>, saldo.month, saldo.year, indx, false)
                }
            }
            Text("${saldo.month} ${saldo.year}", modifier = Modifier.fillMaxSize().padding(top = (1).dp,start = 0.dp).align(Alignment.TopCenter),
                fontFamily = FontFamily.Default, fontSize = 10.sp, fontWeight = FontWeight.Light,
                color = Color.LightGray
            )
        }

    }
}

@Composable
fun verticalList(statement: ArrayList<SaldoStroke>, month: Int, year: Int, indx: Int, isDebet: Boolean) {
    val ctx = CoroutineScope(Dispatchers.Default)

    val rud = remember { mutableStateListOf<SaldoStroke>() }
    LaunchedEffect(true) {
        statement.forEach {
            rud.add(it)
        }
    }
    LazyColumn(modifier = Modifier.width(90.dp).fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
        var bItem: SaldoStroke? = null

        itemsIndexed(items = rud, itemContent = { indx, item ->
            bItem = item
            strokeOfSaldo(item, month, year)
        })
        // circle "plus" for add new stroke of Saldo
        item {
            Row(Modifier.width(100.dp).height(40.dp).clickable {

            }, horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "+", style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}

@Composable
fun strokeOfSaldo(saldoStrokeIn: SaldoStroke, month: Int, year: Int) {
    //val isEdit = remember { saldoState }
    val isEditLocal = remember { mutableStateOf(saldoStrokeIn.isEdit && saldoState.value.saldoAction == SaldoAction.EDITING) }
    var saldoStroke = remember { mutableStateOf(saldoStrokeIn) }
    var saldoStrokeAmount by remember { mutableStateOf("${saldoStrokeIn.amount}") }

    LaunchedEffect(saldoState.value) {
        if (saldoState.value.saldoAction != SaldoAction.EDITING) {
            isEditLocal.value = false
        }
        println("<>>>>"+saldoStroke.value.toString())
    }

    Column(Modifier.fillMaxWidth().clickable {
        saldoState.value = saldoState.value.copy(saldoAction = SaldoAction.EDITING)
        isEditLocal.value = true
        println("<>>> ${saldoState.value.toString()}  ${isEditLocal.value}")
        //cancelEdits.value = true
    }) {
        if (isEditLocal.value && saldoState.value.saldoAction == SaldoAction.EDITING) {
            Column(Modifier.fillMaxWidth().height(100.dp).background(Color.Red)) {
                BasicTextField(
//                colors = BasicTextField.textFieldColors(
//                backgroundColor = Color.White,
//                focusedIndicatorColor =  Color.Transparent, //hide the indicator
//                unfocusedIndicatorColor = Color.Green),
                    modifier = Modifier.fillMaxWidth().height(40.dp).background(Color.Magenta),
                    value = saldoStrokeAmount,
                    onValueChange = {
                        saldoStrokeAmount = it
                        _currentBudget.value[0].wholeStrokes[0].amount = saldoStrokeAmount.toIntOrNull()?: 0

                        if (it.isNotEmpty() && it.isNotBlank()) {

                            //_currentBudget.value = _currentBudget.value[index1].wholeStrokes[index2].copy(amount = saldoStroke.value.amount)
                        }
                    },
                    textStyle = TextStyle.Default.copy(fontSize = 15.sp)
                )
                Box(
                    modifier = Modifier.size(100.dp).clip(RoundedCornerShape(20.dp))
                        .background(if (saldoStroke.value.isConst) Color.Cyan else Color.Yellow)
                        .clickable {
                            saldoStroke.value = saldoStroke.value.copy(isConst = !saldoStroke.value.isConst)

                            refresh(saldoStrokeIn.copy(amount = 222), month, year)
                        }
                ) {
                    Text("const")
                }
            }
        } else {
            Text(text = "${saldoStroke.value.amount} rub", style = MaterialTheme.typography.body1, modifier = Modifier.padding(0.dp))

            //isEditLocal.value = false
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
