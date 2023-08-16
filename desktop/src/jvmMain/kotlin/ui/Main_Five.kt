package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

private val waterFall = MutableSharedFlow<ArrayList<ArrayList<Int>>>()
private var stateFall = arrayListOf<ArrayList<Int>>(
    arrayListOf(1,2,3,10,10,-110),
    arrayListOf(10,10,10,1,2,3),
    arrayListOf(-10,-10,-10,3,2,1)
)
private var resultArray = arrayListOf<ResultSaldo>()

private var isEditMode = mutableStateOf(false)

data class ResultSaldo(val income: Int, val sum: Int, val expense: Int)
fun updateXXX() {
    var lastSum = 0
    stateFall.forEach { month ->
        var incList = month.filter { it != null && it > 0  }
        var expList = month.filter { it != null && it < 0  }

        var income = incList.sum()
        var expense = expList.sum()

        lastSum += income + expense

        resultArray.add(ResultSaldo(income = income,lastSum,expense))
    }

    GlobalScope.async {
//        stateFall.forEachIndexed { index, ints ->
//            stateFall[index].sortDescending()
//        }


        waterFall.emit(arrayListOf())
        waterFall.emit(stateFall)




        println("update-> ${stateFall.joinToString()}")
    }
}

private fun updateStroke(oldValue: Int, newValue: Int?, parentIndex: Int) {
    if (newValue == null) return
    stateFall[parentIndex].forEachIndexed { index, i ->
        if (i == oldValue) {
            stateFall[parentIndex][index] = newValue
            return@forEachIndexed
        }
    }
    updateXXX()
}

private fun addNewStroke(newValue: Int?, parentIndex: Int,isConst: Boolean = false) {
    if (newValue == null) return

    if (parentIndex >= stateFall.size) {
        var newArrayList = arrayListOf<Int>(newValue)


        stateFall.add(newArrayList)
    } else {
        stateFall[parentIndex].add(newValue)

        if (isConst) {
            // add in another saldo`s
            stateFall.forEachIndexed { index, ints ->
                if (index != parentIndex) {
                    stateFall[index].add(newValue)
                }
            }
        }

    }
    updateXXX()
}

private fun delete(monthIndex: Int, value: Int, andFuture: Boolean = false) {
    if (monthIndex < stateFall.size) {
        //stateFall[monthIndex] = ArrayList(stateFall[monthIndex].minus(element = value))
        stateFall[monthIndex].remove(element = value)
        if (andFuture) {
            // remove in another saldo`s
            stateFall[monthIndex].forEachIndexed { indexY, ints ->
                if (indexY != monthIndex && stateFall.size > indexY) {
                    stateFall[indexY] = ArrayList(stateFall[indexY].minus(value))
                }
            }
        }
        //return this
        println("safeDelete: [$value] ${stateFall.joinToString()}")
    }else {
        //return arrayListOf()
        println("ERROR Y >")
    }
    updateXXX()
}

@Composable
fun AppX2() {


    LaunchedEffect(true) {
        println("At start: ${stateFall.joinToString()}")
        updateXXX()
        //tester1()
    }
    val iem = remember { isEditMode }
    val col = waterFall.collectAsState(
        stateFall
    )

    Column(
        Modifier//.fillMaxWidth()
    ) {
        AnimatedVisibility(
            iem.value
        ) {
            Row(
                Modifier.fillMaxWidth().height(50.dp).background(Color.Red).clickable {
                isEditMode.value = false
                //actionSave.value = true
                println("refresh-> ${stateFall.joinToString()}")
                updateXXX()
            }, horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Recalculate", fontSize = 30.sp)
            }
        }
        LazyRow {
//            itemsIndexed(col.value, itemContent = {parentIndex, parentItem ->
//                monthZero(parentItem, parentIndex)
//            })
            col.value.forEachIndexed { parentIndex, parentItem ->
                item {
                    //monthZero(parentItem, parentIndex)
                    var incList = parentItem.filter { it != null && it > 0  }
                    var expList = parentItem.filter { it != null && it < 0  }

                    var income = remember { mutableStateOf(incList.sum()) }
                    var expense = remember { mutableStateOf(expList.sum()) }

                    LaunchedEffect(incList, expList) {
                        income.value = incList.sum()
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
                                    LazyColumn {
                                        itemsIndexed(incList, itemContent = { index, item ->
                                            pizdec(num = item, parentIndex, index)
                                            //Text(">${item}")
                                        })
                                        // circle "plus" for add new stroke of Saldo
                                        item {
                                            plusik(isPositive = true, parentIndex = parentIndex)
                                        }
                                    }
                                }

                                // SUMMA:
                                Column(Modifier.weight(1f).background(Color.White), verticalArrangement = Arrangement.Center) {
                                    Text("${resultArray[parentIndex].income}", modifier = Modifier.padding(vertical = 2.dp),
                                        fontFamily = FontFamily.Default, fontSize = 15.sp, fontWeight = FontWeight.Bold,textAlign = TextAlign.Center,
                                        color = Color.Green
                                    )
                                    Text("${resultArray[parentIndex].sum}", modifier = Modifier.padding(vertical = 5.dp).clickable {
                                        GlobalScope.async {
                                            //waterFall.emit(arrayListOf())
                                            waterFall.emit(arrayListOf())
                                            println("update-> ${stateFall.joinToString()}")
                                        }
                                    },
                                        fontFamily = FontFamily.Default, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold,textAlign = TextAlign.Center,
                                        color = Color.Blue
                                    )
                                    Text("${resultArray[parentIndex].expense}", modifier = Modifier.padding(vertical = 2.dp),
                                        fontFamily = FontFamily.Default, fontSize = 15.sp, fontWeight = FontWeight.Bold,textAlign = TextAlign.Center,
                                        color = Color.Red
                                    )
                                }
                                Row(
                                    Modifier.weight(3f).background(Color.Red)
                                ) {
                                    LazyColumn {
                                        itemsIndexed(expList, itemContent = { index, itemStroke ->
                                            //Text(">${item}")
                                            pizdec(num = itemStroke, parentIndex, index)

                                        })
                                        // circle "plus" for add new stroke of Saldo
                                        item {
                                            plusik(isPositive = false, parentIndex)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun pizdec(num: Int, parentIndex: Int, index: Int) {
    val oldvalue = num
    var isEdit = remember { mutableStateOf(false) }
    var saldoStrokeAmount = remember { mutableStateOf("${num}") }
    var isShowRemoveIcon = remember { mutableStateOf(false) }

    LaunchedEffect(isEditMode.value) {
        if (!isEditMode.value) {
            if (isEdit.value) {
                updateStroke(oldValue = oldvalue, newValue = saldoStrokeAmount.value.toInt(), parentIndex,)
                isEdit.value = false
            }


        }
    }

    Box(Modifier.width(100.dp)
        .background(Color.LightGray)
        .onPointerEvent(PointerEventType.Enter) {
            val position = it.changes.first().position
            //println("posss ${position.toString()}")
            isShowRemoveIcon.value = true
        }.onPointerEvent(PointerEventType.Exit) {
            val position = it.changes.first().position
            //println("posss ${position.toString()}")
            isShowRemoveIcon.value = false
        }
    ) {
        if (isEdit.value) {
            Column(Modifier.width(50.dp).background(Color.Magenta).clickable {

            }) {
                BasicTextField(
                    modifier = Modifier.fillMaxWidth()//.height(40.dp)
                        .background(Color.Magenta),
                    value = saldoStrokeAmount.value,
                    onValueChange = {
                        saldoStrokeAmount.value = it

                        //println("->>${currentBudgetX.value.joinToString()}")
                        //println("stroke ${saldoStrokeAmount.value} ${CalcModule2.currentBudgetX.value.joinToString()}")
                    },
                    textStyle = TextStyle.Default.copy(fontSize = 15.sp)
                )
                Row(Modifier.clickable { delete(monthIndex = parentIndex,value = saldoStrokeAmount.value.toInt(),) }) {
                    Text("Delete", fontSize = 10.sp)
                }
                Row(Modifier.clickable { delete(monthIndex = parentIndex,value = saldoStrokeAmount.value.toInt(),true) }) {
                    Text("Delete and future", fontSize = 10.sp)
                }
            }
        } else {
            //println("$it  ${parentItem.joinToString()}")
            Row(Modifier.fillMaxWidth().clickable {
                GlobalScope.launch {
                    isEditMode.value = true
                    isEdit.value = true
                    //delay(100)
                }

            }) {
                Text("${saldoStrokeAmount.value}")
            }
        }
        if (isShowRemoveIcon.value) {
            Box(Modifier.fillMaxSize().align(Alignment.CenterEnd).background(Color.Red)
                .clickable {
                   delete(monthIndex = parentIndex,value = saldoStrokeAmount.value.toInt(),)

                })
        }
    }

}
@Composable
private fun plusik(isPositive: Boolean = true, parentIndex: Int) {
    var saldoStrokeAmount = remember { mutableStateOf("") }

    LaunchedEffect(isEditMode.value) {
        if (!isEditMode.value && saldoStrokeAmount.value.isNotEmpty() && saldoStrokeAmount.value.isNotBlank()) {

            val newValue = saldoStrokeAmount.value.toInt()
            println("Prep1 ${newValue}")
            addNewStroke(newValue * if(isPositive) 1 else -1, parentIndex)

            saldoStrokeAmount.value = ""
        }
    }

    Row(Modifier.width(100.dp).height(40.dp).clickable {
        isEditMode.value = true
    }, horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        if (isEditMode.value) {
            BasicTextField(
                modifier = Modifier.fillMaxWidth()//.height(40.dp)
                    .background(Color.Magenta),
                value = saldoStrokeAmount.value,
                onValueChange = {
                    if (it.isNotEmpty()) {
                        saldoStrokeAmount.value = it
                    }


                },
                textStyle = TextStyle.Default.copy(fontSize = 15.sp)
            )
        } else {
            Text(text = "+", style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(10.dp)
            )
        }

    }
}

private fun tester1() {
    GlobalScope.launch {
        repeat(100) {
            stateFall = arrayListOf(
                arrayListOf(11,12,13,(-10..20).random()),
                arrayListOf(11,22,23,(-20..30).random()),
                arrayListOf(11,32,33,(-30..40).random()),
            )
            updateXXX()
            delay(10)
        }
    }
}
