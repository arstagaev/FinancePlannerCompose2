package ui

import Consts.saldoState
import SaldoAction
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import colorCredit
//import colorDebit
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow

private val waterFall = MutableSharedFlow<ArrayList<ArrayList<Int>>>()
var stateHolder_OLDSPORT = arrayListOf<ArrayList<Int>>(
    arrayListOf(10,10,10,10,10,-110),
    arrayListOf(10,10,10,10,10,10),
    arrayListOf(-10,-10,-10,10,10,10)
)
fun remover(month: Int, value: Int) {
    if (stateHolder_OLDSPORT.size > month) {

        if (stateHolder_OLDSPORT[month].contains(value)) {

            stateHolder_OLDSPORT[month].remove(value)
            GlobalScope.launch {
                waterFall.emit(arrayListOf())
                waterFall.emit(stateHolder_OLDSPORT)
                println("remover-> ${stateHolder_OLDSPORT.joinToString()}")
            }
        }
    }
}

fun addNew(month: Int, value: Int) {
    stateHolder_OLDSPORT[month].add(value)
    GlobalScope.launch {
        waterFall.emit(arrayListOf())
        waterFall.emit(stateHolder_OLDSPORT)
        println("addNew-> ${stateHolder_OLDSPORT.joinToString()}")
    }
}

fun safeIns(y: Int, value: Int, isConst: Boolean = false) {

    if (y >= stateHolder_OLDSPORT.size) {
        var newArrayList = arrayListOf<Int>(value)


        stateHolder_OLDSPORT.add(newArrayList)
    } else {
        stateHolder_OLDSPORT[y].add(value)
        update()
        if (isConst) {
            // add in another saldo`s
            stateHolder_OLDSPORT.forEachIndexed { index, ints ->
                if (index != y) {
                    stateHolder_OLDSPORT[index].add(value)
                }
            }
        }
    }
}

private fun update() {
    GlobalScope.launch {
        stateHolder_OLDSPORT.forEachIndexed { index, ints ->
            stateHolder_OLDSPORT[index].sortDescending()
        }
        //waterFall.emit(arrayListOf())
        waterFall.emit(stateHolder_OLDSPORT)
        println("update-> ${stateHolder_OLDSPORT.joinToString()}")
    }
}

var insMut = mutableStateOf(0)
var actionSave = mutableStateOf(false)

@Composable
fun TesterThree() {
    val col = waterFall.collectAsState(
        arrayListOf<ArrayList<Int>>(
            arrayListOf(10,10,10,10,10,-110),
            arrayListOf(10,10,10,10,10,10),
            arrayListOf(-10,-10,-10,10,10,10)
        )
    )
    //val mnth = remember { insMut }
    var sldst = remember { saldoState }

    LaunchedEffect(insMut.value) {

    }

    val st = remember { saldoState }
    Column(
        Modifier//.fillMaxWidth()
    ) {
        AnimatedVisibility(
             st.value.saldoAction == SaldoAction.EDITING
        ) {
            Row(Modifier.fillMaxWidth().height(50.dp).background(Color.Red).clickable {
                saldoState.value = saldoState.value.copy(saldoAction = SaldoAction.SHOW)
                actionSave.value = true
                updateXXX()
            }, horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
            ) {
//                    Box(Modifier.fillMaxSize().weight(1f).background(Color.Red).clickable {
//
//                    })
                Text("Recalculate", fontSize = 30.sp)
            }
        }
        LazyRow {
            col.value.forEachIndexed { parentIndex, parentItem ->
                item {
                   // PlateMonth(parentItem, parentIndex)
                    Box(Modifier.width(200.dp)
                        .background(Color.Gray)) {
                        LazyColumn {
                            items(parentItem, itemContent = {

                                pzd(it)

                            })
//                            parentItem.forEach {
//                                item {
//                                    Turka(num = it, parentIndex = parentIndex)
//
//                                }
//                            }
                        }
                    }
                }
            }
//            itemsIndexed(items = col.value, itemContent = { parentIndex, parentItem ->
//                //if (parentItem.size > parentIndex) {
//
//                //}
//
//            })
        }
    }
}

@Composable
fun pzd(it: Int) {
    var isEdit = remember { mutableStateOf(false) }

    LaunchedEffect(saldoState.value.saldoAction) {
        if (saldoState.value.saldoAction == SaldoAction.SHOW) {
            isEdit.value = false
        }
    }

    if (isEdit.value)
        Box(Modifier.background(Color.Red).clickable {
            isEdit.value = false
        }.size(30.dp))
    else
        Box(Modifier.background(Color.Green).clickable {
            isEdit.value = true
            saldoState.value = saldoState.value.copy(saldoAction = SaldoAction.EDITING)
        }) {
            Text("${it}")

        }
}


@Composable
fun PlateMonth(parentItem: ArrayList<Int>, parentIndex: Int) {


    var income = remember { mutableStateOf(0) }
    var expense = remember { mutableStateOf(0) }

    var incList = mutableListOf<Int>()
    var expList = mutableListOf<Int>()


    LaunchedEffect(parentItem, stateHolder_OLDSPORT) {
//        stateHolder[parentIndex].filter { it != null && it > 0 }.forEach {
//            incList.add(it?:0)
//        }
        incList = parentItem.filter { it != null && it > 0  }.toMutableList()
        income.value = incList.sum()
//        stateHolder[parentIndex].filter { it != null && it < 0 }.forEach {
//            expList.add(it?:0)
//        }
        expList = parentItem.filter { it != null && it < 0  }.toMutableList()
        expense.value = expList.sum()
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        elevation = 10.dp
    ) {
        Box(Modifier.clickable {  }) {
            Text("${parentItem.size} ${parentIndex}", modifier = Modifier.fillMaxSize().padding(top = (1).dp,start = 0.dp).align(Alignment.TopCenter),
                fontFamily = FontFamily.Default, fontSize = 10.sp, fontWeight = FontWeight.Light,
                color = Color.LightGray
            )

            Column(
                modifier = Modifier.padding(top = 15.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    Modifier.weight(3f).background(Color.Green))
                {
                    vertL(incList, parentIndex)
                    //verticalList(incList, saldo.month, saldo.year, indxMonth = indxMonth, isDebet = true)
                }
                // SUMMA:
                Column(Modifier.weight(1f).background(Color.White), verticalArrangement = Arrangement.Center) {
                    Text("${income.value}", modifier = Modifier.padding(vertical = 2.dp),
                        fontFamily = FontFamily.Default, fontSize = 15.sp, fontWeight = FontWeight.Bold,textAlign = TextAlign.Center,
                        color = Color.Green
                    )
                    Text("${income.value+expense.value}", modifier = Modifier.padding(vertical = 5.dp),
                        fontFamily = FontFamily.Default, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold,textAlign = TextAlign.Center,
                        color = Color.Blue
                    )
                    Text("${expense.value}", modifier = Modifier.padding(vertical = 2.dp),
                        fontFamily = FontFamily.Default, fontSize = 15.sp, fontWeight = FontWeight.Bold,textAlign = TextAlign.Center,
                        color = Color.Red
                    )
                }
                Row(
                    Modifier.weight(3f).background(Color.Red)
                ) {
                    vertL(expList, indxMonth = parentIndex)
                    //verticalList(expList, saldo.month, saldo.year, indxMonth, false)
                }
            }
        }
    }
}

@Composable
fun vertL(statement: List<Int>, indxMonth: Int,) {
    val ctx = CoroutineScope(Dispatchers.Default)

    //var rud = mutableStateListOf<Int>()
    LaunchedEffect(statement, saldoState.value) {
        //rud.addAll(_itemBudget[indxMonth].filter { it != null && it < 0 })
        //rud.clear()
        //rud.addAll(statement)

        //println("Pipecccc ${statement.joinToString()}")
    }
    LazyColumn(modifier = Modifier.width(90.dp).fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {

        itemsIndexed(items = statement, itemContent = { indxStroke, item ->
            Turka(item?:0, indxMonth)
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

//@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Turka(num: Int, parentIndex: Int) {
    var isEditing = remember { mutableStateOf(true) }
    val isShowRemoveIcon = remember { mutableStateOf(false) }
    var saldoStrokeAmount = remember { mutableStateOf("${num}") }
    var actionInsert = remember { actionSave}

    LaunchedEffect(isEditing.value,saldoState.value, isShowRemoveIcon.value, actionInsert.value) {
        if (saldoState.value.saldoAction == SaldoAction.SHOW) {

            isEditing.value = false
        } else {

        }

        if (isEditing.value) {
            //saldoState.value = saldoState.value.copy(saldoAction = SaldoAction.EDITING)
        } else {
            //saldoState.value = saldoState.value.copy(saldoAction = SaldoAction.SHOW)
        }

        if (actionInsert.value) {
            saldoStrokeAmount.value.toIntOrNull()?.let { safeIns(parentIndex, value = it) }

//            saldoStrokeAmount.value.toIntOrNull()?.let { addNew(parentIndex, it) }
            actionSave.value = false
        }
    }

    Box(Modifier//.width(100.dp)
        .background(Color.LightGray)
//        .onPointerEvent(PointerEventType.Enter) {
//            val position = it.changes.first().position
//            //println("posss ${position.toString()}")
//            isShowRemoveIcon.value = true
//        }.onPointerEvent(PointerEventType.Exit) {
//            val position = it.changes.first().position
//            //println("posss ${position.toString()}")
//            isShowRemoveIcon.value = false
//        }
    ) {
        if (isEditing.value) {
            Box(Modifier.size(50.dp).background(Color.Magenta).clickable {
                isEditing.value = !isEditing.value
            }) {
                BasicTextField(
//                colors = BasicTextField.textFieldColors(
//                backgroundColor = Color.White,
//                focusedIndicatorColor =  Color.Transparent, //hide the indicator
//                unfocusedIndicatorColor = Color.Green),
                    modifier = Modifier.fillMaxWidth().height(40.dp).background(Color.Magenta),
                    value = saldoStrokeAmount.value,
                    onValueChange = {
                        saldoStrokeAmount.value = it

                        //println("->>${currentBudgetX.value.joinToString()}")
                        //println("stroke ${saldoStrokeAmount.value} ${CalcModule2.currentBudgetX.value.joinToString()}")
                    },
                    textStyle = TextStyle.Default.copy(fontSize = 15.sp)
                )
            }
        } else {
            //println("$it  ${parentItem.joinToString()}")
            Row(Modifier.clickable {
                GlobalScope.launch {
                    isEditing.value = true
                    delay(100)
                    saldoState.value = saldoState.value.copy(saldoAction = SaldoAction.EDITING)
                }



                //remover(parentIndex, num)
//                                GlobalScope.launch {
//                                    waterFall.emit(
//                                        arrayListOf<ArrayList<Int>>(
//                                            arrayListOf(10),
//                                            arrayListOf(10,10,10),
//                                            arrayListOf(-10,10,10)
//                                        )
//                                    )
//                                }
            }) {
                Text("pip ${num}")
            }
        }
    }

}

fun String.toIntOrNull() = if (this.isNotBlank() && this.isNotEmpty()) this.toInt() else null