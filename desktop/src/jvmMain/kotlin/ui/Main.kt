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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import colorCredit
import colorDebit
import core.CalcModule2.currentBudgetOUT
import core.CalcModule2.currentBudgetX
import core.prep
import core.safeDelete
import core.safeInserting
import core.safeUpdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import refresh

@Composable
@Preview
fun App() {

    MaterialTheme {

        // val curBud = currentBudget.collectAsState()
        // val ctx = CoroutineScope(Dispatchers.Default)
        val resd = currentBudgetX.collectAsState() //remember { prep(currentBudgetX.value) }
        //val people: State<ArrayList<ArrayList<Int?>>> = currentBudgetX.collectAsState()

        LaunchedEffect(resd) {
            //resd = prep(currentBudgetX.value)
//            currentBudgetX.collect {r ->
//                // fill whole budget by months
//
//                prep(currentBudgetX.value).forEach {
//                    resd.add(it)
//                }
//
//            }
            currentBudgetX.collect {
                println("REFRESH")
            }

        }

        Column {
            val isEdit = remember { saldoState }
            // upper red line, which define we edit now texts
            AnimatedVisibility(
                saldoState.value.saldoAction == SaldoAction.EDITING
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
                itemsIndexed(items = prep(resd.value), itemContent = {indx, item ->
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
fun PlateMonth(saldo: Saldo, indxMonth: Int) {
    var income = saldo.wholeStrokes.filter { it.amount > 0 }.sumOf { it.amount }
    var expense = saldo.wholeStrokes.filter { it.amount < 0 }.sumOf { it.amount }
    LaunchedEffect(saldo) {
        income = saldo.wholeStrokes.filter { it.amount > 0 }.sumOf { it.amount }
        expense = saldo.wholeStrokes.filter { it.amount < 0 }.sumOf { it.amount }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        elevation = 10.dp
    ) {
        Box(Modifier.clickable {  }) {
            Text("${saldo.month} ${saldo.year}", modifier = Modifier.fillMaxSize().padding(top = (1).dp,start = 0.dp).align(Alignment.TopCenter),
                fontFamily = FontFamily.Default, fontSize = 10.sp, fontWeight = FontWeight.Light,
                color = Color.LightGray
            )

            Column(
                modifier = Modifier.padding(top = 15.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    Modifier.weight(3f).background(colorDebit))
                {
                    verticalList(saldo.wholeStrokes.filter { it.nature == Nature.DEBIT } as ArrayList<SaldoStroke>, saldo.month, saldo.year, indxMonth, isDebet = true)
                }
                // SUMMA:
                Column(Modifier.weight(1f).background(Color.White), verticalArrangement = Arrangement.Center) {
                    Text("${income}", modifier = Modifier.padding(vertical = 2.dp),
                        fontFamily = FontFamily.Default, fontSize = 15.sp, fontWeight = FontWeight.Bold,textAlign = TextAlign.Center,
                        color = Color.Green
                    )
                    Text("${income+expense}", modifier = Modifier.padding(vertical = 5.dp),
                        fontFamily = FontFamily.Default, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold,textAlign = TextAlign.Center,
                        color = Color.Blue
                    )
                    Text("${expense}", modifier = Modifier.padding(vertical = 2.dp),
                        fontFamily = FontFamily.Default, fontSize = 15.sp, fontWeight = FontWeight.Bold,textAlign = TextAlign.Center,
                        color = Color.Red
                    )
                }
                Row(
                    Modifier.weight(3f).background(colorCredit)
                ) {
                    verticalList(saldo.wholeStrokes.filter { it.nature == Nature.CREDIT } as ArrayList<SaldoStroke>, saldo.month, saldo.year, indxMonth, false)
                }
            }

        }

    }
}

@Composable
fun verticalList(statement: ArrayList<SaldoStroke>, month: Int, year: Int, indxMonth: Int, isDebet: Boolean) {
    val ctx = CoroutineScope(Dispatchers.Default)

    val rud = remember { mutableStateListOf<SaldoStroke>() }
    LaunchedEffect(statement) {
        statement.forEach {
            rud.add(it)
        }
    }
    LazyColumn(modifier = Modifier.width(90.dp).fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
        var bItem: SaldoStroke? = null

        itemsIndexed(items = rud, itemContent = { indxStroke, item ->
            bItem = item
            strokeOfSaldo(item, month, year, indxStroke,indxMonth)
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
fun strokeOfSaldo(saldoStrokeIn: SaldoStroke, month: Int, year: Int, indxStroke: Int, indxMonth: Int) {
    //val isEdit = remember { saldoState }
    val ctx = CoroutineScope(Dispatchers.Default)
    val isEditLocal = remember { mutableStateOf(saldoStrokeIn.isEdit && saldoState.value.saldoAction == SaldoAction.EDITING) }
    var saldoStroke = remember { mutableStateOf(saldoStrokeIn) }
    var saldoStrokeAmount by remember { mutableStateOf("${saldoStrokeIn.amount}") }

    LaunchedEffect(saldoState.value, saldoStrokeIn.amount) {
        if (saldoState.value.saldoAction != SaldoAction.EDITING) {
            isEditLocal.value = false
        }
        saldoStrokeAmount = saldoStrokeIn.amount.toString()
        //println("<>>>>"+saldoStroke.value.toString())
    }

    Column(Modifier.fillMaxWidth().clickable {
        saldoState.value = saldoState.value.copy(saldoAction = SaldoAction.EDITING)
        isEditLocal.value = true
        println("<>>> ${saldoState.value.toString()}  ${isEditLocal.value}")
        //cancelEdits.value = true
    }) {
        if (isEditLocal.value && saldoState.value.saldoAction == SaldoAction.EDITING) {
            Column(Modifier.fillMaxWidth().height(160.dp).background(Color.Red)) {
                BasicTextField(
//                colors = BasicTextField.textFieldColors(
//                backgroundColor = Color.White,
//                focusedIndicatorColor =  Color.Transparent, //hide the indicator
//                unfocusedIndicatorColor = Color.Green),
                    modifier = Modifier.fillMaxWidth().height(40.dp).background(Color.Magenta),
                    value = saldoStrokeAmount,
                    onValueChange = {
                        saldoStrokeAmount = it
                        currentBudgetX.value.safeUpdate(indxMonth,indxStroke,saldoStrokeAmount.toInt())
                        println("->>${currentBudgetX.value.joinToString()}")
                    },
                    textStyle = TextStyle.Default.copy(fontSize = 15.sp)
                )
                Box(
                    modifier = Modifier.fillMaxWidth().height(40.dp).clip(RoundedCornerShape(20.dp))
                        .background(if (saldoStroke.value.isConst) Color.Cyan else Color.Yellow)
                        .clickable {
                            saldoStroke.value = saldoStroke.value.copy(isConst = !saldoStroke.value.isConst)
                            currentBudgetX.value.safeUpdate(indxMonth,indxStroke,saldoStrokeAmount.toInt(),isConst = true)
                        }
                ) {
                    Text("repeat in feature ->", modifier = Modifier.padding(vertical = 5.dp),
                        fontFamily = FontFamily.Default, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold,textAlign = TextAlign.Center,
                        color = Color.Blue
                    )
                }
                Box(
                    modifier = Modifier.fillMaxWidth().height(40.dp).clip(RoundedCornerShape(20.dp))
                        .background(if (saldoStroke.value.isConst) Color.Cyan else Color.Yellow)
                        .clickable {
                            saldoStroke.value = saldoStroke.value.copy(isConst = !saldoStroke.value.isConst)
                            ctx.launch {
                                currentBudgetX.safeDelete(indxMonth,value = saldoStrokeAmount.toInt(),andFuture = false)

//                                currentBudgetX.emit(currentBudgetX.value.safeDelete(indxMonth,value = saldoStrokeAmount.toInt(),andFuture = false))

                            }
                            //refresh(saldoStrokeIn.copy(amount = 222), month, year)
                        }
                ) {
                    Text("delete in cell ->", modifier = Modifier.padding(vertical = 5.dp),
                        fontFamily = FontFamily.Default, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold,textAlign = TextAlign.Center,
                        color = Color.Blue
                    )
                }
                Box(
                    modifier = Modifier.fillMaxWidth().height(40.dp).clip(RoundedCornerShape(20.dp))
                        .background(if (saldoStroke.value.isConst) Color.Cyan else Color.Yellow)
                        .clickable {
                            saldoStroke.value = saldoStroke.value.copy(isConst = !saldoStroke.value.isConst)

                            currentBudgetX.value.safeDelete(indxMonth,indxStroke,andFuture = true)
                            //refresh(saldoStrokeIn.copy(amount = 222), month, year)
                        }
                ) {
                    Text("delete in future ->", modifier = Modifier.padding(vertical = 5.dp),
                        fontFamily = FontFamily.Default, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold,textAlign = TextAlign.Center,
                        color = Color.Blue
                    )
                }
            }
        } else {
            Text(text = "${saldoStrokeAmount} rub", style = MaterialTheme.typography.body1, modifier = Modifier.padding(0.dp))

            //isEditLocal.value = false
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
