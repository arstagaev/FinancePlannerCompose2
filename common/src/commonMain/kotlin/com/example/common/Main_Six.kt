package com.example.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.custom.DatePickerUI
import com.example.common.custom.DateSelectionSection
import com.example.common.custom.currentDay
import com.example.common.custom.currentMonth
import com.example.common.custom.currentYear
import com.example.common.custom.defineNumMonth
import com.example.common.custom.monthsNames
import com.example.common.utils.toIntSafe
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import kotlinx.serialization.Serializable

val colorDebit = Color(0xFF57DE5d)//Color(68,220,96)
val colorCredit = Color(0xFFDE7171)//Color(220,63,96)

val colorDebitResult = Color(68,200,96)
val colorCreditResult = Color(200,63,96)

val colorDebitStroke = Color(204,255,229)
val colorCreditStroke = Color(255,204,204)

val colorTextDebitTitle = Color(12, 144, 63 )
val colorTextCreditTitle = Color(144, 12, 63 )

var ShowWithDescription = true
var stateFall = arrayListOf<ArrayList<SaldoCell>>(
    arrayListOf(SaldoCell(amount = 1), SaldoCell(amount = 1), SaldoCell(1), SaldoCell(1)),
    arrayListOf(SaldoCell(amount = 1), SaldoCell(amount = 1), SaldoCell(1), SaldoCell(-1)),
    arrayListOf(SaldoCell(amount = 12), SaldoCell(amount = 1), SaldoCell(1, isConst = true), SaldoCell(111))
)
var configurationOfSaldo = mutableStateOf<SaldoConfiguration>(
    SaldoConfiguration(
        investmentsAmount = -404,
        investmentsName = "404"
    )
)

private val waterFall = MutableSharedFlow<ArrayList<ArrayList<SaldoCell>>>()
private val resultFall = MutableSharedFlow<ArrayList<ResultSaldo>>()
val futureFall = mutableStateOf<FutureSaldo?>(null)

private var resultArray = arrayListOf<ResultSaldo>()

var isEditMode = mutableStateOf(false)
var inputDateMode = mutableStateOf(false)

private var year = 2023
private var month = 1
private var startDate = LocalDate(year,month,1)//LocalDateTime.of(2023,1,1)


@Serializable
data class SaveContainer(val data: ArrayList<ArrayList<SaldoCell>>)

@Serializable
data class SaldoConfiguration(
    var investmentsAmount: Int,
    var investmentsName: String,
)

data class ResultSaldo(
    val date: LocalDate? = null, val income: Int, val sum: Int, val expense: Int//, var isForecast: Boolean = false//, val arrayIncome: ArrayList<Int>, val arrayExpense: ArrayList<Int>
)

data class FutureSaldo(
    val income: Int,
    var startForecastDate: LocalDate? = null,
    val sum1: Int,
    val sum2: Int,
    val sum3: Int,
    val expense: Int, var incomes: List<Int>, var expenses: List<Int>,
    var periodHalfYear: Int? = null,
    var periodFirstYear: Int? = null,
    var periodSecondYear: Int? = null,
)

@Serializable
data class SaldoCell(var amount: Int, var name: String = "", var isConst: Boolean = false)

fun initital() {
    CoroutineScope(CoroutineName("Init")).launch {
        decodeFromFile()
        updateWhole()
    }
}
fun updateWhole() {
    startDate = LocalDate(year = year, monthNumber = month,1)
    var investments = configurationOfSaldo.value.investmentsAmount
    resultArray.clear()

    // make result
    var lastSum = configurationOfSaldo.value.investmentsAmount//-1000//investments

    // for result
    var deltaForFuture = 0
    var incConst = 0
    var expConst = 0
    var futureIncome = listOf<Int>()
    var futureExpense = listOf<Int>()
    var dt: LocalDate? = null

    stateFall.forEachIndexed { index, month ->
        var incList = ArrayList(month.filter { it != null && it.amount > 0  })
        var expList = ArrayList(month.filter { it != null && it.amount < 0  })

        var income = incList.sumOf { it.amount }
        var expense = expList.sumOf { it.amount }

        lastSum += income + expense
        println("<stateFall.forEachIndexed>>>> lastSum:${lastSum}  income:${income} expense:${expense}  investmentsAmount VM:${configurationOfSaldo.value.investmentsAmount}")
        dt = startDate.plus(DatePeriod(months = index))
        resultArray.add(ResultSaldo(date = dt, income = income, sum = lastSum, expense = expense))

        if (index == stateFall.size-1) {
            futureIncome = incList.filter { it.isConst }.map { it.amount }
            futureExpense = expList.filter { it.isConst }.map { it.amount }

            incConst = futureIncome.sum()
            expConst = futureExpense.sum()

            deltaForFuture = incConst + expConst
        }
    }

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
            6 -> sumHalfYear = cumulative
            12 -> sumYear = cumulative
            24 -> sumSecondYear = cumulative
            else -> {}
        }
    }

    var forecast = FutureSaldo(
        income = incConst, expense = expConst,
        startForecastDate =dt,
        sum1 = sum1,
        sum2 = sum2,
        sum3 = sum3,
        incomes = futureIncome, expenses = futureExpense,
        periodHalfYear = sumHalfYear,
        periodFirstYear = sumYear,
        periodSecondYear = sumSecondYear
    )

    GlobalScope.async {
        resultFall.emit(arrayListOf())
        resultFall.emit(resultArray)

        waterFall.emit(arrayListOf())
        waterFall.emit(stateFall)

        //futureFall.emit(null)
        futureFall.value = forecast
        println("===========================>")
        println("~refresh stateFall-> ${stateFall.joinToString()}")
        println("~refresh resultArray-> ${resultArray.joinToString()}")
        println("~refresh forecast-> ${forecast.toString()}")
        println("<===========================")
    }
}

private fun updateStroke(oldSaldo: SaldoCell, newSaldoCell: SaldoCell, parentIndex: Int) {
    if (newSaldoCell.amount == null) return

    stateFall[parentIndex].forEachIndexed { index, i ->

        if (i == oldSaldo) {
            stateFall[parentIndex][index] = newSaldoCell
            println("updateStroke:> ${stateFall[parentIndex].map { it.amount }.joinToString()}")
            updateWhole()
            return
        }
    }
    stateFall[parentIndex].sortedBy { it.amount }
}

private fun addNewSaldo() {
    var toFuture = stateFall.last().filter { it.isConst }

    stateFall.add(toFuture as ArrayList<SaldoCell>)

    updateWhole()
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
    updateWhole()
}
// TODO need check:
private fun deleteCell(monthIndex: Int, saldoCell: SaldoCell, andFuture: Boolean = false) {
    if (monthIndex < stateFall.size) {
        //stateFall[monthIndex] = ArrayList(stateFall[monthIndex].minus(element = value))
        val indexRemovedElement = stateFall[monthIndex].indexOf(saldoCell)
        stateFall[monthIndex].removeAt(indexRemovedElement)

        if (andFuture) {
            stateFall.forEachIndexed { index, saldoCells ->
                if (index >= monthIndex && stateFall.size > index) {
                    val ire = stateFall[index].indexOf(saldoCell)
                    println(">>> ${ire} ${saldoCells.joinToString()}")
                    if (ire >= 0) {
                        stateFall[index].removeAt(ire)
                    }
                }
            }
        }
        println("safeDelete: [$saldoCell] ${stateFall.joinToString()}")
    } else {
        println("ERROR Y >")
    }
    updateWhole()
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun AppX2() {
    val crtcxt = rememberCoroutineScope()
    LaunchedEffect(true) {
        println("At start: ${stateFall.joinToString()}")
        updateWhole()
        //tester1()
    }
    val iem = remember { isEditMode }
    val idm = remember { inputDateMode }
    val col = waterFall.collectAsState(
        stateFall
    )

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            Modifier.fillMaxSize() , verticalArrangement = Arrangement.SpaceAround//.fillMaxSize().background(colorBackgroundDark)//.fillMaxWidth()
        ) {
            AnimatedVisibility(
                idm.value
            ) {
                Column(
                    Modifier.fillMaxWidth()//.height(50.dp)
                        .background(Color.White).clickable {
//                    isEditMode.value = false
//                    updateWhole()
                        }, verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.CenterHorizontally
                ) {
//                crtcxt.launch {
//                    encodeForSave()
//                }
                    val chosenYear = remember { mutableStateOf(currentYear) }
                    val chosenMonth = remember { mutableStateOf(currentMonth) }

                    Text(modifier = Modifier.width(300.dp), text = "Choose started date:", color = colorTextCreditTitle, fontSize = 30.sp)

                    Spacer(modifier = Modifier.height(30.dp))

                    DateSelectionSection(
                        onYearChosen = {
                            //chosenYear.value = it.toIntSafe()
                            year = it.toIntSafe()
                                       },
                        onMonthChosen = {
                            //chosenMonth.value = monthsNames.map { it.name }.indexOf(it)
                            month = defineNumMonth(it)
                        }
                    )

                    Spacer(modifier = Modifier.fillMaxWidth().height(30.dp))

                    Button(
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        onClick = {
                            updateWhole()
                            inputDateMode.value = false
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
            AnimatedVisibility(
                iem.value
            ) {
                Row(
                    Modifier.fillMaxWidth().height(50.dp).background(colorCredit).clickable {
                        isEditMode.value = false
                        //actionSave.value = true
                        //println("refresh-> ${stateFall.joinToString()}")
                        updateWhole()
                    }, horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
                ) {
                    crtcxt.launch {
                        encodeForSave()
                    }

                    Text("Recalculate", color = colorTextCreditTitle, fontSize = 30.sp)
                }
            }
            LazyRow(Modifier.fillMaxSize().background(
                colorGrayWindow2
                //Color.White
                //colorBackgroundDark2
            )) {
//            itemsIndexed(col.value, itemContent = {parentIndex, parentItem ->
//                monthZero(parentItem, parentIndex)
//            })

                item {
                    InitialInvestments()
                }
                col.value.forEachIndexed { parentIndex, parentItem ->
                    item {
                        val res = resultFall.collectAsState(resultArray)

                        Card(
                            modifier = Modifier
                                .width(150.dp)
                                .padding(5.dp),
                            shape = RoundedCornerShape(10.dp)
                            //elevation = 10.dp
                        ) {
                            Box(Modifier.fillMaxSize().background(
                                colorCard
                                //Color.LightGray
                            )) {
                                val dt = if (res.value.size > parentIndex) res.value[parentIndex].date else null
                                Text("${dt?.year} ${dt?.month} ", modifier = Modifier.fillMaxWidth().padding(top = (1).dp,start = 10.dp).align(Alignment.TopCenter)
                                    .clickable {
                                        inputDateMode.value = !inputDateMode.value
                                    }
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
                                        Row(modifier = Modifier.fillMaxWidth().background(colorDebitResult)) {
                                            Text("Σ Income: ${if (res.value.size > parentIndex) res.value[parentIndex].income else 0}", modifier = Modifier.padding(vertical = 2.dp),
                                                fontFamily = FontFamily.Default, fontSize = 10.sp, fontWeight = FontWeight.Bold,textAlign = TextAlign.Center,
                                                color = colorTextDebitTitle
                                            )
                                        }

                                        Text("${ if (res.value.size > parentIndex) res.value[parentIndex].sum else 0}", modifier = Modifier.basicMarquee(iterations = 10).padding(vertical = 5.dp).clickable {
                                            GlobalScope.async {
                                                updateWhole()
                                            }
                                        },
                                            fontFamily = FontFamily.Default, fontSize = 25.sp, fontWeight = FontWeight.ExtraBold,textAlign = TextAlign.Center,
                                            overflow = TextOverflow.Ellipsis,
                                            color = colorTextSumMonth //Color.DarkGray
                                        )
                                        Row(modifier = Modifier.fillMaxWidth().background(colorCreditResult)) {
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

        Card(modifier = Modifier.size(60.dp).padding(10.dp).align(Alignment.BottomStart), elevation = 15.dp, shape = RoundedCornerShape(14.dp)) {
            Box(modifier = Modifier.fillMaxSize().clickable {
                inputDateMode.value = !inputDateMode.value
            }) {
                Icon(modifier = Modifier.align(Alignment.Center),imageVector = Icons.Filled.Settings, contentDescription = "Settings")

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
    var detailShow = remember { mutableStateOf(ShowWithDescription) }
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
    Card (Modifier.fillMaxWidth().padding(top = 2.dp, bottom = 2.dp, start = 5.dp, end = 5.dp),
        shape = RoundedCornerShape(5.dp),
        elevation = 10.dp) {

        Column(
            Modifier.fillMaxSize()//.width(100.dp)
                .background(if (isIncome) colorDebitStroke else colorCreditStroke)
        ) {
            if (isEdit.value) {
                TextField(
                    modifier = Modifier.fillMaxWidth()//.height(40.dp)
                        .background(if (isIncome) colorDebitStroke else colorCreditStroke),
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
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Row(Modifier.clickable {
                        deleteCell(monthIndex = parentIndex, saldoCell = saldoCell)
                        isEditMode.value = false
                        //isEdit.value = false

                    }) {
                        Text("❌", fontSize = 20.sp)
                    }
                    Row(Modifier.clickable {
                        deleteCell(monthIndex = parentIndex, saldoCell = saldoCell, true)
                        //isEdit.value = false
                        isEditMode.value = false
                    }) {
                        Text("❌\uD83D\uDD1C", fontSize = 20.sp)
                    }
                }

            } else {
                val text1 = buildAnnotatedString {
                    withStyle(SpanStyle(color = if (isIncome) colorDebitResult else colorCreditResult, fontWeight = FontWeight.Bold)) {
                        append("${saldoCell.amount}")
                    }
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
                    }, horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (!detailShow.value) {
                        Text(modifier = Modifier.padding(start = 5.dp), text = "${saldoCell.amount} ")
                        Text(modifier = Modifier.padding(start = 5.dp, end = 5.dp), text ="${if (saldoCell.isConst) "\uD83D\uDD04" else ""}")
                    } else {
                        Text(modifier = Modifier.padding(start = 5.dp), text =text1)
                        Text(modifier = Modifier.padding(start = 5.dp, end = 5.dp), text ="${if (saldoCell.isConst) "\uD83D\uDD04" else ""}")
                    }

                }
            }
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
                arrayListOf(SaldoCell(1), SaldoCell(amount = (-10..20).random())),
                arrayListOf(SaldoCell(1), SaldoCell(amount = (-20..30).random())),
                arrayListOf(SaldoCell(1), SaldoCell(amount = (-30..40).random())),
            )
            updateWhole()
            delay(10)
        }
    }
}
