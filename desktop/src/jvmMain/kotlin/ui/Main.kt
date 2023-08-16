package ui

import Consts._currentBudget
import Consts.currentBudget
import Consts.saldoState
import Saldo
import SaldoAction
import SaldoState
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
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
import core.*
import core.CalcModule2.currentBudgetOUT
import core.CalcModule2.currentBudgetX
import kotlinx.coroutines.*


@Composable
fun verticalList(statement: List<Int?>, month: Int, year: Int, indxMonth: Int, isDebet: Boolean) {
    val ctx = CoroutineScope(Dispatchers.Default)

    var rud = mutableStateListOf<Int>()
//    LaunchedEffect(saldoState.value, itemBudget[indxMonth].size) {
//        //rud.addAll(_itemBudget[indxMonth].filter { it != null && it < 0 })
//        //rud.clear()
//
//        itemBudget[indxMonth].forEach {
//            if (it!=null ) {
//                if (isDebet && it > 0 ) {
//                    rud.add(it?:0)
//                } else if (!isDebet && it < 0) {
//                    rud.add(it?:0)
//                }
//            }
//        }
//        //println("Pipecccc ${statement.joinToString()}")
//    }
    LazyColumn(modifier = Modifier.width(90.dp).fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {

        itemsIndexed(items = rud, itemContent = { indxStroke, item ->
            strokeOfSaldo(item?:0, indxStroke, indxMonth)
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
// fun SaldoState.invalidate() {
//    GlobalScope.launch {
//        saldoState.value = SaldoState(SaldoAction.EDITING)
//        delay(500)
//        saldoState.value = SaldoState(SaldoAction.SHOW)
//    }
//
//}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun strokeOfSaldo(saldoStrokeIn: Int, indxStroke: Int, indxMonth: Int) {
    val ctx = CoroutineScope(Dispatchers.Default)
    var isEditInternal = false
    var isEditLocal = remember { mutableStateOf(isEditInternal && saldoState.value.saldoAction == SaldoAction.EDITING) }

    val isShowRemoveIcon = remember { mutableStateOf(false) }
    var saldoStrokeAmount = remember { mutableStateOf(
        "${currentBudgetX.value[indxMonth][indxStroke]}"
        //"$saldoStrokeIn"
    ) }

    LaunchedEffect(saldoState.value, isShowRemoveIcon.value) {
        if (saldoState.value.saldoAction != SaldoAction.EDITING) {
            isEditLocal.value = false
            isEditInternal = false
        } else {

        }
        //saldoStrokeAmount = saldoStrokeIn.toString()
        //println("<>>>>pizdec "+_itemBudget[indxMonth].joinToString())
    }

    Box(Modifier.fillMaxWidth().clickable {
        ctx.launch {
            isEditInternal = true
            saldoState.value = saldoState.value.copy(saldoAction = SaldoAction.EDITING)
            //delay(100)
            isEditLocal.value = true
            println("<>>> ${saldoState.value.toString()}  ${isEditLocal.value}")
        }

        //cancelEdits.value = true
    }.onPointerEvent(PointerEventType.Enter) {
            val position = it.changes.first().position
            //println("posss ${position.toString()}")
            isShowRemoveIcon.value = true
    }.onPointerEvent(PointerEventType.Exit) {
            val position = it.changes.first().position
            //println("posss ${position.toString()}")
            isShowRemoveIcon.value = false
    }
    ) {
        Column(Modifier.fillMaxSize()) {
            //Text("${isEditLocal.value}")
            if (isEditLocal.value) {
                Column(Modifier.fillMaxWidth().height(160.dp).background(Color.Red)) {
                    BasicTextField(
//                colors = BasicTextField.textFieldColors(
//                backgroundColor = Color.White,
//                focusedIndicatorColor =  Color.Transparent, //hide the indicator
//                unfocusedIndicatorColor = Color.Green),
                        modifier = Modifier.fillMaxWidth().height(40.dp).background(Color.Magenta),
                        value = saldoStrokeAmount.value,
                        onValueChange = {
                            saldoStrokeAmount.value = it
                            //currentBudgetX.value.safeUpdate(indxMonth,indxStroke,saldoStrokeAmount.value.toInt())
                            //println("->>${currentBudgetX.value.joinToString()}")
                            println("stroke ${saldoStrokeAmount.value} ${currentBudgetX.value.joinToString()}")
                        },
                        textStyle = TextStyle.Default.copy(fontSize = 15.sp)
                    )
                    Box(
                        modifier = Modifier.fillMaxWidth().height(40.dp).clip(RoundedCornerShape(20.dp))
                            //.background(if (saldoStroke.value.isConst) Color.Cyan else Color.Yellow)
                            .clickable {
                                //saldoStroke.value = saldoStroke.value.copy(isConst = !saldoStroke.value.isConst)
                               // currentBudgetX.value.safeUpdate(indxMonth,indxStroke,saldoStrokeAmount.value.toInt(),isConst = true)
                            }
                    ) {
                        Text("repeat in feature ->", modifier = Modifier.padding(vertical = 5.dp),
                            fontFamily = FontFamily.Default, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold,textAlign = TextAlign.Center,
                            color = Color.Blue
                        )
                    }
                }
            } else {
                Text(text = "${saldoStrokeAmount.value} rub", style = MaterialTheme.typography.body1, modifier = Modifier.padding(0.dp))

                //isEditLocal.value = false
            }
        }
        if (isShowRemoveIcon.value) {

            Box(Modifier.size(30.dp).align(Alignment.CenterEnd).background(Color.Red).clickable {
                //_itemBudget.safeDelete(indxMonth,value = saldoStrokeAmount.value.toInt(),andFuture = false)
                //saldoState.value.invalidate()
            })
        }

    }

}

fun main() = application {

    Window(onCloseRequest = ::exitApplication) {

        //App()
        //Test()
        //Tester()
        //TesterThree()
        updateXXX()
        AppX2()
    }
}
private val _itemList = mutableStateListOf<String>()
val itemList: List<String> = _itemList
@Composable
fun Test() {
    MaterialTheme {
        LaunchedEffect(true) {
            _itemList.add("Zero")
            _itemList.add("Zero")
            _itemList.add("Zero")
        }
        LazyColumn(Modifier.fillMaxWidth()) {
            itemsIndexed(items = itemList, itemContent = { indxStroke, item ->
                Text(modifier = Modifier.clickable {
                    _itemList.remove("Zero")
                }, text = "$item <---", fontSize = 20.sp)
            })
        }
    }
}
