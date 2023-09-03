package com.example.common.ui.main_dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.colorGrayWindow2
import com.example.common.colorText
import com.example.common.decodeFromFile
import com.example.common.saveNewBudgetJSON
import com.example.common.enums.SaldoMode
import com.example.common.models.FutureSaldo
import com.example.common.models.MonthSaldo
import com.example.common.models.ResultSaldo
import com.example.common.models.SaldoCell
import com.example.common.models.SaldoConfiguration
import com.example.common.ui.main_screen.EditorOfDate
import com.example.common.ui.main_screen.InitialInvestments
import com.example.common.ui.main_screen.PlateOfMonth
import com.example.common.ui.main_screen.forecastGhostMonth
import com.example.common.ui.main_screen.longForecast
import com.example.common.ui.main_screen.shimmerEffectBlue
import com.example.common.utils.generatePaybackPeriod
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import java.time.LocalDateTime

val colorDebit = Color(0xFF57DE5d)//Color(68,220,96)
val colorCredit = Color(0xFFDE7171)//Color(220,63,96)

val colorDebitResult = Color(68,200,96)
val colorCreditResult = Color(200,63,96)

val colorDebitStroke = Color(204,255,229)
val colorCreditStroke = Color(255,204,204)

val colorTextDebitTitle = Color(12, 144, 63 )
val colorTextCreditTitle = Color(144, 12, 63 )

var configurationOfSaldo = mutableStateOf<SaldoConfiguration>(
    SaldoConfiguration(
        investmentsAmount = 0,
        investmentsName = "input here description",
        startedDateMonth = 2,
        startedDateYear = 1997
    )
)

private var startDate = LocalDate(
    configurationOfSaldo.value.startedDateYear,
    configurationOfSaldo.value.startedDateMonth,
    1
)//LocalDateTime.of(2023,1,1)

var ShowWithDescription = true
var stateFall = arrayListOf<MonthSaldo>(
//    arrayListOf(SaldoCell(amount = 1), SaldoCell(amount = 1), SaldoCell(1), SaldoCell(1)),
//    arrayListOf(SaldoCell(amount = 1), SaldoCell(amount = 1), SaldoCell(1), SaldoCell(-1)),
//    arrayListOf(SaldoCell(amount = 12), SaldoCell(amount = 1), SaldoCell(1, isConst = true), SaldoCell(111))
)


private val waterFall = MutableSharedFlow<ArrayList<MonthSaldo>>()
internal val resultFall = MutableSharedFlow<ArrayList<ResultSaldo>>()
val futureFall = mutableStateOf<FutureSaldo?>(null)
val paybackPeriod = mutableStateOf<String>("2+ years")
internal var resultArray = arrayListOf<ResultSaldo>()

var isEditMode = mutableStateOf(false)
var saldoMode = mutableStateOf<SaldoMode>(SaldoMode.LOADING)

val showTips = mutableStateOf<Boolean>(false)

//fun initital() {
//
//    CoroutineScope(CoroutineName("Init")).launch {
//
//        //delay(1000L)
//        //updateWhole()
//    }
//}
fun updateWhole() {
    val crtScp = CoroutineScope(CoroutineName("Update"))
    //saldoMode.value = SaldoMode.LOADING
    resultArray.clear()
    futureFall.value = null
    // make result
    var lastSum = configurationOfSaldo.value.investmentsAmount//-1000//investments

    // for result
    var deltaForFuture = 0
    var incConst = 0
    var expConst = 0
    var futureIncome = listOf<Int>()
    var futureExpense = listOf<Int>()
    var dt: LocalDate? = null
    startDate = LocalDate(
        configurationOfSaldo.value.startedDateYear,
        configurationOfSaldo.value.startedDateMonth,
        1
    )
    //LocalDateTime.of(2023,1,1)
    println("> stateFall ${stateFall.joinToString()}")


    /**
        in initial launch of app , when empty
    */

    if (stateFall.isEmpty()) {
        saldoMode.value = SaldoMode.SHOW

        resultArray.add(
            ResultSaldo(LocalDate(year = LocalDateTime.now().year,LocalDateTime.now().month,LocalDateTime.now().dayOfMonth),0,0,0)
        )
        configurationOfSaldo.value = configurationOfSaldo.value.copy(startedDateYear = LocalDateTime.now().year, startedDateMonth = LocalDateTime.now().monthValue)
        crtScp.launch {

            resultFall.emit(resultArray)
            futureFall.value = FutureSaldo(
                income = incConst, expense = expConst,
                startForecastDate = startDate,
                sum1 = 0,
                sum2 = 0,
                sum3 = 0,
                incomes = futureIncome, expenses = futureExpense,
                periodHalfYear = 0,
                periodFirstYear = 0,
                periodSecondYear = 0
            )
        }
        return
    }
    var alreadyPayback = false
    stateFall.forEachIndexed { index, month ->
        //stateFall[index] = month.sortedBy { it.amount } as ArrayList<SaldoCell>
        //stateFall[index] = ArrayList( month.sortedBy { it.amount })

//        var incList = ArrayList(month.filter { it != null && it.amount > 0  })
//        var expList = ArrayList(month.filter { it != null && it.amount < 0  })

        var income = month.incomes.sumOf { it.amount }
        var expense = month.expenses.sumOf { it.amount }

        lastSum = income + expense + lastSum
        println("<stateFall.forEachIndexed>>>> lastSum:${lastSum}  income:${income} expense:${expense}  investmentsAmount VM:${configurationOfSaldo.value.investmentsAmount} || ${month.toString()}")
        dt = startDate.plus(DatePeriod(months = index))
        resultArray.add(ResultSaldo(date = dt, income = income, sum = lastSum, expense = expense))

        if (!alreadyPayback && lastSum > 0) {
            paybackPeriod.value = generatePaybackPeriod(index+1)
            alreadyPayback = true
        }

        // future generate
        if (index == stateFall.size-1) {
            futureIncome = month.incomes.filter { it.isConst }.map { it.amount }
            futureExpense = month.expenses.filter { it.isConst }.map { it.amount }

            incConst = futureIncome.sum()
            expConst = futureExpense.sum()

            deltaForFuture = incConst + expConst
        }
    }

    // future generate
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

        if (!alreadyPayback && cumulative > 0) {
            paybackPeriod.value = generatePaybackPeriod(resultArray.size+1+it)
            alreadyPayback = true
        }

    }

    val forecast = FutureSaldo(
        income = incConst, expense = expConst,
        startForecastDate = dt,
        sum1 = sum1,
        sum2 = sum2,
        sum3 = sum3,
        incomes = futureIncome, expenses = futureExpense,
        periodHalfYear = sumHalfYear,
        periodFirstYear = sumYear,
        periodSecondYear = sumSecondYear
    )

    crtScp.launch {
        println("===========================>")

        resultFall.emit(arrayListOf())
        resultFall.emit(resultArray)

        waterFall.emit(arrayListOf())
        waterFall.emit(stateFall)

        //futureFall.emit(null)

        futureFall.value = forecast


        println("~refresh stateFall-> ${stateFall.joinToString()}")
        println("~refresh resultArray-> ${resultArray.joinToString()}")
        println("~refresh forecast-> ${forecast.toString()}")

        saldoMode.value = SaldoMode.SHOW
        saveNewBudgetJSON()
        println("<===========================")
    }

}

internal fun updateStroke(oldSaldo: SaldoCell, newSaldoCell: SaldoCell, parentIndex: Int, isIncome: Boolean) {
    if (newSaldoCell.amount == null) return
    if (isIncome) {
        stateFall[parentIndex].incomes.forEachIndexed { index, i ->

            if (i == oldSaldo) {
                stateFall[parentIndex].incomes[index] = newSaldoCell
                println("updateStroke:> ${stateFall[parentIndex].incomes.map { it.amount }.joinToString()}")
                updateWhole()
                return
            }
        }
        stateFall[parentIndex].incomes.sortedBy { it.amount }
    } else {
        stateFall[parentIndex].expenses.forEachIndexed { index, i ->

            if (i == oldSaldo) {
                stateFall[parentIndex].expenses[index] = newSaldoCell
                println("updateStroke:> ${stateFall[parentIndex].expenses.map { it.amount }.joinToString()}")
                updateWhole()
                return
            }
        }
        stateFall[parentIndex].expenses.sortedBy { it.amount }
    }

}

private fun addNewSaldo() {
    if (stateFall.isEmpty()) {
        return
    }

    var toFuture = stateFall.last().copy(
        incomes = ArrayList(stateFall.last().incomes.filter { it.isConst }),
        expenses = ArrayList(stateFall.last().expenses.filter { it.isConst })
    )//.filter { it.isConst }

    stateFall.add(toFuture)

    updateWhole()
}

internal fun addNewCell(newValue: SaldoCell?, parentIndex: Int, isIncome: Boolean) {
    if (newValue == null) return

    if (parentIndex >= stateFall.size) {
        var newArrayList = arrayListOf<SaldoCell>(newValue)

        stateFall.add(MonthSaldo(
            incomes = if (isIncome) newArrayList else arrayListOf(),
            expenses = if (isIncome) arrayListOf() else newArrayList
        ))

    } else {
        if (isIncome) {
            stateFall[parentIndex].incomes.add(newValue)
            stateFall[parentIndex].incomes.sortedBy { it.amount }
        } else {
            stateFall[parentIndex].expenses.add(newValue)
            stateFall[parentIndex].expenses.sortedBy { it.amount }
        }


        if (newValue.isConst) {
            var afterMatchCurrentMonth = false
            // add in another saldo`s
            stateFall.forEachIndexed { index, ints ->

                if (index == parentIndex) {
                    afterMatchCurrentMonth = true
                }
                if (index != parentIndex && afterMatchCurrentMonth) {

                    if (isIncome) {
                        stateFall[index].incomes.add(newValue)
                    }else {
                        stateFall[index].expenses.add(newValue)
                    }

                }
            }
        }
    }
    println("const:${newValue.isConst} addNewStroke[ ${stateFall.joinToString()} ] ")
    updateWhole()
}
// TODO need check:
internal fun deleteCell(monthIndex: Int, saldoCell: SaldoCell, andFuture: Boolean = false, isIncome: Boolean) {
    if (monthIndex < stateFall.size) {
        //stateFall[monthIndex] = ArrayList(stateFall[monthIndex].minus(element = value))
        if (isIncome) {
            val indexRemovedElement = stateFall[monthIndex].incomes.indexOf(saldoCell)
            stateFall[monthIndex].incomes.removeAt(indexRemovedElement)
        } else {
            val indexRemovedElement = stateFall[monthIndex].expenses.indexOf(saldoCell)
            stateFall[monthIndex].expenses.removeAt(indexRemovedElement)
        }


        if (andFuture) {
            stateFall.forEachIndexed { index, saldoCells ->
                if (index >= monthIndex && stateFall.size > index) {

                    if (isIncome) {
                        val ire = stateFall[index].incomes.indexOf(saldoCell)
                        println(">>> ${ire} ${saldoCells.incomes.joinToString()}")
                        if (ire >= 0) { stateFall[index].incomes.removeAt(ire) }
                    } else {
                        val ire = stateFall[index].expenses.indexOf(saldoCell)
                        println(">>> ${ire} ${saldoCells.expenses.joinToString()}")
                        if (ire >= 0) { stateFall[index].expenses.removeAt(ire) }
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
fun BudgetScreen(component: MainDashboardComponent) {

    val crtcxt = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val isAtStart by remember {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            lazyListState.firstVisibleItemIndex == 0
//            else {
//                val lastVisibleItem = visibleItemsInfo.last()
//                val viewportHeight = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset
//
//                (lastVisibleItem.index + 1 == layoutInfo.totalItemsCount &&
//                        lastVisibleItem.offset + lastVisibleItem.size <= viewportHeight)
//            }
        }
    }
    LaunchedEffect(Unit) {
        saldoMode.value = SaldoMode.LOADING
        decodeFromFile()
        delay(1000L)
        updateWhole()
        saldoMode.value = SaldoMode.SHOW
    }
    val iem = remember { isEditMode }
    val idm = remember { saldoMode }
    val col = waterFall.collectAsState(
        stateFall
    )
    val paybackPeriod_Internal = remember { paybackPeriod }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier.fillMaxSize() , verticalArrangement = Arrangement.SpaceAround//.fillMaxSize().background(colorBackgroundDark)//.fillMaxWidth()
        ) {
            AnimatedVisibility(
                idm.value == SaldoMode.SETUP_SETTINGS
            ) {
                EditorOfDate()
            }
            AnimatedVisibility(
                iem.value
            ) {
                Row(
                    Modifier.fillMaxWidth().shimmerEffectBlue().height(50.dp).clickable {
                        actionToSaveChanges()
                    }, horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
                ) {

                    Text("Save Changes", color = colorGrayWindow2, fontSize = 30.sp)
                }
            }
            LazyRow(Modifier.fillMaxSize().background(
                colorGrayWindow2
            ),state = lazyListState) {

                item {
                    InitialInvestments()
                }

                if (col.value.isNotEmpty()) {
                    col.value.forEachIndexed { parentIndex, parentItem ->
                        item {
                            PlateOfMonth(parentIndex, parentItem)
                        }
                    }
                } else {
                    item {
                        PlateOfMonth(0,
                            MonthSaldo(
                                incomes = arrayListOf(),
                                expenses = arrayListOf()
                            )
                        )
                    }
                }


                item {
                    Column(Modifier.fillMaxHeight().padding(top = 15.dp), verticalArrangement = Arrangement.Top) {
                        Box(modifier = Modifier.clickable {
                            addNewSaldo()
                        }
                            //.padding(4.dp)
                            .size(30.dp)
                            .aspectRatio(1f)
                            .background(colorText, shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(modifier = Modifier, text = "+", color= colorGrayWindow2,   textAlign = TextAlign.Center)
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

            Box(Modifier.fillMaxWidth().height(100.dp).background(Color.Red))
        }
        Column(modifier = Modifier.padding(10.dp).align(Alignment.BottomStart)) {
            Row(modifier = Modifier, horizontalArrangement = Arrangement.SpaceBetween) {
                if (isAtStart) {
                    Card(modifier = Modifier.size(60.dp).padding(10.dp), elevation = 15.dp, shape = RoundedCornerShape(14.dp)) {
                        Box(modifier = Modifier.fillMaxSize().clickable {
                            showTips.value = !showTips.value
                        }) {
                            Icon(modifier = Modifier.align(Alignment.Center),imageVector = Icons.Filled.Info, contentDescription = "Settings")
                        }
                    }
                }
            }

            Card(modifier = Modifier.size(60.dp).padding(10.dp), elevation = 15.dp, shape = RoundedCornerShape(14.dp)) {
                Box(modifier = Modifier.fillMaxSize().clickable {
                    if (saldoMode.value == SaldoMode.SHOW) {
                        saldoMode.value = SaldoMode.SETUP_SETTINGS
                    } else {
                        updateWhole()
                        saldoMode.value = SaldoMode.SHOW
                    }
                }) {
                    Icon(modifier = Modifier.align(Alignment.Center),imageVector = Icons.Filled.Settings, contentDescription = "Settings")
                }
            }

            if (isAtStart) {
                Card(modifier = Modifier.size(60.dp).padding(10.dp), elevation = 15.dp, shape = RoundedCornerShape(14.dp)) {
                    Box(modifier = Modifier.fillMaxSize().clickable {
                        configurationOfSaldo.value = SaldoConfiguration(
                            investmentsAmount = 0,
                            investmentsName = "input here description",
                            startedDateMonth = 2,
                            startedDateYear = 1997
                        )
                        isEditMode.value = false
                        //showBudget.value = false
                        showTips.value = false
                        component.toBackListOfBudgets()
//                            isEditMode.value = false
//                            showTips.value = false
                    }) {
                        Icon(modifier = Modifier.align(Alignment.Center),imageVector = Icons.Filled.ArrowBack, contentDescription = "Settings")
                    }
                }
            }

//            Row(modifier = Modifier, horizontalArrangement = Arrangement.SpaceBetween) {}
        }



        Card(modifier = Modifier.width(250.dp)
            .height(65.dp).padding(10.dp).align(Alignment.BottomCenter), elevation = 15.dp, shape = RoundedCornerShape(14.dp)) {
            Row(modifier = Modifier.fillMaxSize().background(Color.White).clickable {
                if (saldoMode.value == SaldoMode.SHOW) {
                    saldoMode.value = SaldoMode.SETUP_SETTINGS
                } else {
                    saldoMode.value = SaldoMode.SHOW
                }
            }, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround) {
                Text(modifier = Modifier.padding(4.dp), text = "Payback period:", color= Color.Black, textAlign = TextAlign.Center)
                Text(modifier = Modifier.padding(4.dp), text = paybackPeriod_Internal.value, color= Color.Black, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,style = MaterialTheme.typography.body1)
            }
        }

//        Row(modifier = positionTips.value.modifier.align(Alignment.TopStart), horizontalArrangement = Arrangement.SpaceEvenly) {
//            Text("\uD83D\uDC46", fontSize = 50.sp)
//            Column(Modifier.padding(top = 8.dp), verticalArrangement = Arrangement.SpaceBetween) {
//                Text(modifier= Modifier.background(Color.LightGray).alpha(0.5f),
//                    text = "kasjlfaalkfj",color= Color.Black, fontSize = 30.sp)
//                Text(modifier= Modifier.shimmerEffectBlue().clickable {
//                    changePositionOfTips()
//                }, text = "next tip",color= Color.Black, fontSize = 30.sp)
//            }
//        }

        //Box(Modifier.fillMaxSize().background(Color.Black).alpha(0.4f).clip())

    }
}

fun actionToSaveChanges() {
    CoroutineScope(CoroutineName("Action to save")).launch {
        isEditMode.value = false
        //updateWhole()
        saveNewBudgetJSON()
    }
}

private fun tester1() {
//    GlobalScope.launch {
//        repeat(100) {
//            stateFall = arrayListOf(
//                arrayListOf(SaldoCell(1), SaldoCell(amount = (-10..20).random())),
//                arrayListOf(SaldoCell(1), SaldoCell(amount = (-20..30).random())),
//                arrayListOf(SaldoCell(1), SaldoCell(amount = (-30..40).random())),
//            )
//            updateWhole()
//            delay(10)
//        }
//    }
}
