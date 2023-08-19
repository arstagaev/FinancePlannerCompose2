package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

val colorDebit = Color(68,242,96)
val colorCredit = Color(242,63,96)

val colorDebitResult = Color(68,200,96)
val colorCreditResult = Color(200,63,96)

val colorDebitCard = Color(204,255,229)
val colorCreditCard = Color(255,204,204)

private val waterFall = MutableSharedFlow<ArrayList<ArrayList<SaldoCell>>>()
private val resultFall = MutableSharedFlow<ArrayList<ResultSaldo>>()
val futureFall = mutableStateOf<FutureSaldo?>(null)
private var stateFall = arrayListOf<ArrayList<SaldoCell>>(
    arrayListOf(SaldoCell(amount = 1),SaldoCell(amount = 1),SaldoCell(1),SaldoCell(1)),
    arrayListOf(SaldoCell(amount = 1),SaldoCell(amount = 1),SaldoCell(1),SaldoCell(-1)),
    arrayListOf(SaldoCell(amount = 12),SaldoCell(amount = 1),SaldoCell(1, isConst = true),SaldoCell(111))
)
private var resultArray = arrayListOf<ResultSaldo>()

private var isEditMode = mutableStateOf(false)

data class ResultSaldo(
    val income: Int, val sum: Int, val expense: Int//, var isForecast: Boolean = false//, val arrayIncome: ArrayList<Int>, val arrayExpense: ArrayList<Int>
)
data class FutureSaldo(
    val investments: Int,
    val income: Int,
    val sum1: Int,
    val sum2: Int,
    val sum3: Int,
    val expense: Int, var incomes: List<Int>, var expenses: List<Int>,
    var periodHalfYear: Int? = null,
    var periodFirstYear: Int? = null,
    var periodSecondYear: Int? = null,
)

data class SaldoCell(var amount: Int, var name: String? = null, var isConst: Boolean = false)

fun updateXXX() {
    var investments = 100
    resultArray.clear()

    // make result
    var lastSum = investments

    // for result
    var deltaForFuture = 0
    var incConst = 0
    var expConst = 0
    var futureIncome = listOf<Int>()
    var futureExpense = listOf<Int>()

    stateFall.forEachIndexed { index, month ->
        var incList = ArrayList(month.filter { it != null && it.amount > 0  })
        var expList = ArrayList(month.filter { it != null && it.amount < 0  })

        var income = incList.sumOf { it.amount }
        var expense = expList.sumOf { it.amount }

        lastSum += income + expense

        resultArray.add(ResultSaldo(income = income, sum = lastSum, expense = expense))
        if (index == stateFall.size-1) {
            futureIncome = incList.filter { it.isConst }.map { it.amount }
            futureExpense = expList.filter { it.isConst }.map { it.amount }

            incConst = futureIncome.sum()
            expConst = futureExpense.sum()

            deltaForFuture = incConst + expConst
        }
    }

    // make forecast
//    val lastMonthConsts = stateFall.last().filter { it.isConst }
//    val incomeConst = lastMonthConsts.filter { it.amount > 0 }.map { it.amount }
//    val expenseConst = lastMonthConsts.filter { it.amount < 0 }.map { it.amount }
//
//    val sumIncConst = incomeConst.sum()
//    val sumExpConst = expenseConst.sum()
//
//    val delta = sumIncConst - sumExpConst

    val sum1 = resultArray.last().sum + deltaForFuture //lastSum + delta + resultArray.last().sum
    val sum2 = sum1 + deltaForFuture
    val sum3 = sum2 + deltaForFuture

    var cumulative = sum3
    var sumHalfYear: Int? = null
    var sumYear: Int?  = null
    var sumSecondYear: Int?  = null

    repeat(25) {
        cumulative += deltaForFuture

        when(it) {
            6 -> {
                sumHalfYear = cumulative
            }
            12 -> {
                sumYear = cumulative
            }
            24 -> {
                sumSecondYear = cumulative
            }
            else -> {}
        }
    }

    var forecast = FutureSaldo(investments = investments,
        income = incConst, expense = expConst,
        sum1 = sum1,
        sum2 = sum2,
        sum3 = sum3,
        incomes = futureIncome, expenses = futureExpense,
        periodHalfYear = sumHalfYear,
        periodFirstYear = sumYear,
        periodSecondYear = sumSecondYear
    )

    GlobalScope.async {
//        stateFall.forEachIndexed { index, ints ->
//            stateFall[index].sortDescending()
//        }
        resultFall.emit(arrayListOf())
        resultFall.emit(resultArray)

        waterFall.emit(arrayListOf())
        waterFall.emit(stateFall)

        //futureFall.emit(null)
        futureFall.value = forecast



        println("refresh-> ${stateFall.joinToString()}")
    }
}

private fun updateStroke(oldSaldo: SaldoCell, newSaldoCell: SaldoCell, parentIndex: Int) {
    if (newSaldoCell.amount == null) return

    stateFall[parentIndex].forEachIndexed { index, i ->

        if (i == oldSaldo) {
            stateFall[parentIndex][index] = newSaldoCell
            println("updateStroke:> ${stateFall[parentIndex].map { it.amount }.joinToString()}")
            updateXXX()
            return
        }
    }
    stateFall[parentIndex].sortedBy { it.amount }
}

private fun addNewSaldo() {
    var toFuture = stateFall.last().filter { it.isConst }

    stateFall.add(toFuture as ArrayList<SaldoCell>)

    updateXXX()
}

private fun addNewCell(newValue: SaldoCell?, parentIndex: Int) {
    if (newValue == null) return

    if (parentIndex >= stateFall.size) {
        var newArrayList = arrayListOf<SaldoCell>(newValue)


        stateFall.add(newArrayList)
    } else {
        stateFall[parentIndex].add(newValue)
        stateFall[parentIndex].sortedBy { it.amount }

        if (newValue.isConst) {
            var afterMatchCurrentMonth = false
            // add in another saldo`s
            stateFall.forEachIndexed { index, ints ->

                if (index == parentIndex) {
                    afterMatchCurrentMonth = true
                }
                if (index != parentIndex && afterMatchCurrentMonth) {

                    stateFall[index].add(newValue)
                }
            }
        }

    }
    println("const:${newValue.isConst} addNewStroke[ ${stateFall.joinToString()} ] ")
    updateXXX()
}
// TODO need check:
private fun deleteCell(monthIndex: Int, saldoCell: SaldoCell, andFuture: Boolean = false) {
    if (monthIndex < stateFall.size) {
        //stateFall[monthIndex] = ArrayList(stateFall[monthIndex].minus(element = value))
        stateFall[monthIndex].remove(element = saldoCell)
//        stateFall[monthIndex].forEachIndexed { index, saldoCell ->
//            if ()
//            stateFall[monthIndex].removeAt(index)
//        }
        if (andFuture) {
            // remove in another saldo`s
            stateFall[monthIndex].forEachIndexed { indexY, ints ->
                if (indexY != monthIndex && stateFall.size > indexY) {
                    stateFall[indexY] = ArrayList(stateFall[indexY].minus(saldoCell))
                }
            }
        }
        //return this
        println("safeDelete: [$saldoCell] ${stateFall.joinToString()}")
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
            item {
                var futureSaldo = remember { futureFall }
                val textInvest = buildAnnotatedString {
                    withStyle(SpanStyle(color = Color.Blue)) {
                        append("Initial investments:")
                    }
                    append("\n" + "${futureSaldo.value?.investments}")
                }
                Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                    Card(
                        modifier = Modifier
                            .width(150.dp)
                            .height(100.dp)
                            .padding(5.dp),
                        elevation = 10.dp
                    ) {
                        Box(Modifier.fillMaxSize()) {
                            Text(textInvest,
                                modifier = Modifier.padding(4.dp).align(Alignment.Center)
                                    .clickable {},
                                fontSize = 10.sp, fontFamily = FontFamily.Monospace,
                                //color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                        }

                    }
                }
            }
            col.value.forEachIndexed { parentIndex, parentItem ->
                item {
                    //monthZero(parentItem, parentIndex)
//                    var incList = parentItem.filter { it != null && it.amount > 0  }
//                    var expList = parentItem.filter { it != null && it.amount < 0  }
//
//                    var income = remember { mutableStateOf(incList.sum()) }
//                    var expense = remember { mutableStateOf(expList.sum()) }
//
//                    LaunchedEffect(incList, expList) {
//                        income.value = incList.sum()
//                        expense.value = expList.sum()
//                    }
                    val res = resultFall.collectAsState(resultArray)

                    Card(
                        modifier = Modifier
                            .width(150.dp)
                            .padding(5.dp),
                        //elevation = 10.dp
                    ) {
                        Box(Modifier.fillMaxSize().background(Color.LightGray).clickable {  }) {
                            Text("${parentItem.size} ${parentIndex}", modifier = Modifier.fillMaxSize().padding(top = (1).dp,start = 0.dp).align(Alignment.TopCenter),
                                fontFamily = FontFamily.Default, fontSize = 10.sp, fontWeight = FontWeight.Light,
                                color = Color.LightGray
                            )

                            Column(
                                modifier = Modifier.fillMaxSize().padding(top = 15.dp), horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    Modifier.weight(3f).background(colorDebit))
                                {
                                    LazyColumn {
                                        itemsIndexed(
                                            parentItem.filter { it.amount > 0 },
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
                                Column(Modifier.weight(1f), verticalArrangement = Arrangement.SpaceBetween) {
                                    Row(modifier = Modifier.fillMaxWidth().background(colorDebitResult)) {
                                        Text("Σ Income: ${if (res.value.size > parentIndex) res.value[parentIndex].income else 0}", modifier = Modifier.padding(vertical = 2.dp),
                                            fontFamily = FontFamily.Default, fontSize = 10.sp, fontWeight = FontWeight.Bold,textAlign = TextAlign.Center,
                                            //color = Color.Green
                                        )
                                    }

                                    Text("${ if (res.value.size > parentIndex) res.value[parentIndex].sum else 0}", modifier = Modifier.padding(vertical = 5.dp).clickable {
                                        GlobalScope.async {
                                            updateXXX()
                                        }
                                    },
                                        fontFamily = FontFamily.Default, fontSize = 25.sp, fontWeight = FontWeight.ExtraBold,textAlign = TextAlign.Center,
                                        color = Color.DarkGray
                                    )
                                    Row(modifier = Modifier.fillMaxWidth().background(colorCreditResult)) {
                                        Text(
                                            "Σ Expense: ${if (res.value.size > parentIndex) res.value[parentIndex].expense else 0}",
                                            modifier = Modifier.padding(vertical = 2.dp),
                                            fontFamily = FontFamily.Default,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center,
                                            //color = Color.Red
                                        )
                                    }
                                }

                                Row(
                                    Modifier.weight(3f).background(colorCredit)
                                ) {
                                    LazyColumn {
                                        itemsIndexed(parentItem.filter { it.amount < 0 }, itemContent = { index, itemStroke ->
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
            }
            item {
                Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                    Box(modifier = Modifier.clickable {
                        addNewSaldo()
                    }
                        //.padding(4.dp)
                        .size(30.dp)
                        .aspectRatio(1f)
                        .background(Color.White, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(modifier = Modifier, text = "+", color= Color.Black,   textAlign = TextAlign.Center)
                    }
                }

            }
            item {
                forecastGhostMonth(1)
            }
            item {
                forecastGhostMonth(2)
            }
            item {
                forecastGhostMonth(3)
            }
            item {
                longForecast()
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun strokeAgregator(saldoCell: SaldoCell, parentIndex: Int, index: Int, isIncome: Boolean = true) {
    if (!stateFall[parentIndex].contains(saldoCell)) return

    val oldvalue = saldoCell.amount
    var isEdit = remember { mutableStateOf(false) }
    var detailShow = remember { mutableStateOf(false) }
    var saldoStrokeAmount = remember { mutableStateOf("${saldoCell.amount}") }
    var saldoStrokeName = remember { mutableStateOf("${saldoCell.name}") }
    var isShowRemoveIcon = remember { mutableStateOf(false) }

    LaunchedEffect(isEditMode.value) {
        if (!isEditMode.value) {
            if (isEdit.value) {
                updateStroke(oldSaldo = saldoCell, newSaldoCell = SaldoCell(amount = saldoStrokeAmount.value.toInt(), name = saldoStrokeName.value, saldoCell.isConst), parentIndex,)
                isEdit.value = false
            }
        }
    }
    Card (Modifier.fillMaxWidth().padding(1.dp),
        shape = RoundedCornerShape(5.dp),
        elevation = 10.dp) {

        Column(
            Modifier.fillMaxSize()//.width(100.dp)
                .background(if (isIncome) colorDebitCard else colorCreditCard)
        ) {
            if (isEdit.value) {
                TextField(
                    modifier = Modifier.fillMaxWidth()//.height(40.dp)
                        .background(if (isIncome) colorDebitCard else colorCreditCard),
                    value = saldoStrokeAmount.value.toString(),
                    onValueChange = {
                        val newNum = it.filter { it.isDigit() }
                        if (newNum.isNotEmpty()) {
                            saldoStrokeAmount.value = newNum
                        }
                        //println("->>${currentBudgetX.value.joinToString()}")
                        //println("stroke ${saldoStrokeAmount.value} ${CalcModule2.currentBudgetX.value.joinToString()}")
                    },
                    textStyle = TextStyle.Default.copy(fontSize = 15.sp)
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Row(Modifier.clickable { deleteCell(monthIndex = parentIndex, saldoCell = saldoCell) }) {
                        Text("❌", fontSize = 20.sp)
                    }
                    Row(Modifier.clickable { deleteCell(monthIndex = parentIndex, saldoCell = saldoCell, true) }) {
                        Text("❌\uD83D\uDD1C", fontSize = 20.sp)
                    }
                }

            } else {
                val text1 = buildAnnotatedString {
                    append("${saldoCell.amount} ${if (saldoCell.isConst) "\uD83D\uDD04" else ""} ")
                    withStyle(SpanStyle(color = Color.LightGray)) {
                        append("\n" + "${saldoCell.name}")
                    }
                }
                Row(Modifier.fillMaxWidth()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {},
                            onDoubleTap = {
                            },
                            onLongPress = {
                                detailShow.value = !detailShow.value
                            },
                            onTap = {
                                GlobalScope.launch {
                                    isEditMode.value = true
                                    isEdit.value = true
                                    //delay(100)
                                }
                            }
                        )
                    }
                ) {
                    if (!detailShow.value) {
                        Text(modifier = Modifier.padding(start = 5.dp), text = "${saldoCell.amount} ${if (saldoCell.isConst) "\uD83D\uDD04" else ""}")
                    } else {
                        Text(modifier = Modifier.padding(start = 5.dp), text =text1)
                    }

                }


            }
//        if (isShowRemoveIcon.value) {
//            Box(Modifier.fillMaxSize().align(Alignment.CenterEnd).background(Color.Red)
//                .clickable {
//                    deleteCell(monthIndex = parentIndex,value = saldoStrokeAmount.value)
//                })
//        }
        }
    }
}

@Composable
private fun plusik(isIncome: Boolean = true, parentIndex: Int) {
    var saldoStrokeAmount = remember { mutableStateOf("") }
    var saldoStrokeName = remember { mutableStateOf("") }
    val checkedState = remember { mutableStateOf(false) }
    //var saldoStrokeName = remember { mutableStateOf("") }
    var isEdit = remember { mutableStateOf(false) }
    var newCellSaldo = remember { mutableStateOf<SaldoCell>(SaldoCell(amount = 0,name = "")) }
    //var isEditByHuman = remember { mutableStateOf(false) }

    LaunchedEffect(isEditMode.value) {
        if (!isEditMode.value && saldoStrokeAmount.value.toString().isNotEmpty() && saldoStrokeAmount.value.toString().isNotBlank()) {
            if (isEdit.value) {
                val newValue = saldoStrokeAmount.value.toInt() ?: 0// newCellSaldo.value.amount.toInt()
                println("Prep1 ${newValue} ${checkedState.value}")

                //addNewCell(SaldoCell(amount = newValue * if(isIncome) 1 else -1), parentIndex)
                addNewCell(newCellSaldo.value.copy(amount = newValue * if (isIncome) 1 else -1, isConst = checkedState.value, name = saldoStrokeName.value), parentIndex)

                newCellSaldo.value.amount = 0
                isEdit.value = false
            }
        }
        if (!isEditMode.value) {
            if (isEdit.value) {
                //updateStroke(oldValue = oldvalue, newValue = saldoStrokeAmount.value.toInt(), parentIndex,)
                isEdit.value = false
            }
        }
    }

    Row(Modifier.fillMaxWidth()//.height(40.dp)
        .clickable {
        isEditMode.value = true
        isEdit.value = true
    }, horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Top) {
        if (isEdit.value) {
            Column {
                TextField(
                    modifier = Modifier.fillMaxWidth()//.height(40.dp)
                        .background(Color.Transparent),
                    //value = newCellSaldo.value.amount.toString(),
                    value = saldoStrokeAmount.value,
                    onValueChange = {
                        val newNum = it.filter { it.isDigit() }
                        if (newNum.isNotEmpty()) {
                            saldoStrokeAmount.value = newNum
                        }
//                        if (it.isNotEmpty()) {
//                            //newCellSaldo.value.amount = it.toInt()
//                            saldoStrokeAmount.value = it
//                            //isEditByHuman.value = true
//                        }


                    },
                    label = { Text("Enter new amount", fontSize = 10.sp) },
                    textStyle = TextStyle.Default.copy(fontSize = 12.sp)
                )
                TextField(
                    modifier = Modifier.fillMaxWidth()//.height(40.dp)
                        .background(Color.Transparent),
                    value = saldoStrokeName.value?:"",

                    onValueChange = {
                        if (it.isNotEmpty()) {
                            saldoStrokeName.value = it
                            //newCellSaldo.value.name = it
                            //isEditByHuman.value = true
                        }
                    },
                    label = { Text("Enter name for source of amount", fontSize = 10.sp) },
                    textStyle = TextStyle.Default.copy(fontSize = 10.sp),

                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = checkedState.value,
                        onCheckedChange = {
                            checkedState.value = it
                            //isEditByHuman.value = true
                        },
                        modifier = Modifier.size(20.dp)
                    )
                    Text(modifier = Modifier.padding(start = 5.dp, bottom = 10.dp), text = "is is permanent ${if(isIncome) "income" else "expense"}", fontSize = 12.sp)
                }
            }

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
                arrayListOf(SaldoCell(1),SaldoCell(amount = (-10..20).random())),
                arrayListOf(SaldoCell(1),SaldoCell(amount = (-20..30).random())),
                arrayListOf(SaldoCell(1),SaldoCell(amount = (-30..40).random())),
            )
            updateXXX()
            delay(10)
        }
    }
}
