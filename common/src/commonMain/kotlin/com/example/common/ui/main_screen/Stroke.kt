package com.example.common.ui.main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.colorTextSecondary
import com.example.common.getPlatformName
import com.example.common.models.SaldoCell
import com.example.common.ui.main_dashboard.actionToSaveChanges
import com.example.common.ui.main_dashboard.colorCreditResult
import com.example.common.ui.main_dashboard.colorCreditStroke
import com.example.common.ui.main_dashboard.colorDebitResult
import com.example.common.ui.main_dashboard.colorDebitStroke
import com.example.common.ui.main_dashboard.colorTextCreditTitle
import com.example.common.ui.main_dashboard.colorTextDebitTitle
import com.example.common.ui.main_dashboard.deleteCell
import com.example.common.ui.main_dashboard.isEditMode
import com.example.common.ui.main_dashboard.showTips
import com.example.common.ui.main_dashboard.updateStroke
import com.example.common.utils.Platform
import com.example.common.utils.currency
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun strokeAgregator(saldoCell: SaldoCell, parentIndex: Int, index: Int, isIncome: Boolean = true) {
    //if (if (stateFall.size > parentIndex) !stateFall[parentIndex].contains(saldoCell) else return) return

    val oldvalue = saldoCell.amount
    var isEditLocal = remember { mutableStateOf(false) }
    //var detailShow = remember { mutableStateOf(ShowWithDescription) }
    var saldoStrokeAmount = remember { mutableStateOf(TextFieldValue(text = "${saldoCell.amount}", selection = TextRange( "${saldoCell.amount}".length))) }
    var saldoStrokeName = remember { mutableStateOf("${saldoCell.name}") }
    var isShowRemoveIcon = remember { mutableStateOf(false) }
    // initialize focus reference to be able to request focus programmatically
    val focusRequesterAmount = remember { FocusRequester() }
    val focusRequesterText = remember { FocusRequester() }
    val showTipsInternal = remember { showTips }

    LaunchedEffect(isEditMode.value) {

        if (!isEditMode.value && saldoStrokeAmount.value.toString().isNotEmpty() && saldoStrokeAmount.value.toString().isNotBlank()) {
            if (isEditLocal.value) {
                updateStroke(oldSaldo = saldoCell, newSaldoCell = SaldoCell(amount = saldoStrokeAmount.value.text.toInt(), name = saldoStrokeName.value, saldoCell.isConst), parentIndex = parentIndex, isIncome = isIncome)
                actionToSaveChanges()
                isEditLocal.value = false

            }
        }
        if (!isEditMode.value) {
            if (isEditLocal.value) {
                isEditLocal.value = false
            }
        }
        if (isEditLocal.value) {
            focusRequesterAmount.requestFocus()
        }
    }
    Card (
        Modifier.fillMaxWidth().padding(top = 2.dp, bottom = 2.dp, start = 5.dp, end = 5.dp),
        shape = RoundedCornerShape(5.dp),
        elevation = 10.dp) {
        val colorFieldsDebit = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent, textColor = colorTextDebitTitle,
            focusedLabelColor = colorTextDebitTitle, unfocusedLabelColor = colorTextSecondary,
            placeholderColor = colorTextDebitTitle,
            unfocusedIndicatorColor = colorTextSecondary, focusedIndicatorColor = colorTextDebitTitle
        )

        val colorFieldsCredit = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent, textColor = colorTextCreditTitle,
            focusedLabelColor = colorTextCreditTitle, unfocusedLabelColor = colorTextSecondary,
            placeholderColor = colorTextCreditTitle,
            unfocusedIndicatorColor = colorTextSecondary, focusedIndicatorColor = colorTextCreditTitle
        )
        Column(
            if (showTipsInternal.value)
                Modifier.fillMaxSize()//.width(100.dp)
                    .shimmerEffectBlue()
            else
                Modifier.fillMaxSize()//.width(100.dp)
                    .background(if (isIncome) colorDebitStroke else colorCreditStroke)
        ) {
            if (isEditLocal.value) {

                TextField(
                    modifier = Modifier.fillMaxWidth()//.height(40.dp)
                        .background(Color.Transparent).onKeyEvent {
                            if (it.key == Key.Enter) {
                                //actionToSaveChanges()
                                isEditMode.value = false
                                true
                            }
                            if (it.key == Key.Escape) {
                                isEditLocal.value = false
                                isEditMode.value = false
                                true
                            }
                            if (it.key == Key.Tab) {
                                focusRequesterText.requestFocus()
                                true
                            }
                            false
                        }// add focusRequester modifier
                        .focusRequester(focusRequesterAmount)
                    ,
                    //value = newCellSaldo.value.amount.toString(),
                    value = saldoStrokeAmount.value,
                    colors = if (isIncome) colorFieldsDebit else colorFieldsCredit,
                    onValueChange = { newStroke ->
                        val newNum = newStroke.text.filter { it.isDigit() || it == '-' }
                        if (newNum.isNotEmpty()) {
                            saldoStrokeAmount.value = TextFieldValue(text = newNum, selection = newStroke.selection)
                        }
                    },
                    label = { Text("Amount", color = if (isIncome) colorTextDebitTitle else colorTextCreditTitle, fontSize = 8.sp) },
                    textStyle = TextStyle.Default.copy(fontSize = 20.sp, color = if (isIncome) colorTextDebitTitle else colorTextCreditTitle, fontWeight = FontWeight.Bold),

                    keyboardOptions = if (getPlatformName() == Platform.ANDROID)
                        KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ) else
                            KeyboardOptions.Default,
                    keyboardActions = KeyboardActions(
                        onDone = { actionToSaveChanges() },
                        onSearch = { actionToSaveChanges() },
                        onGo = { actionToSaveChanges() },
                        onNext = { actionToSaveChanges() },
                        onSend = { actionToSaveChanges() }
                    )
                )
                TextField(
                    modifier = Modifier.fillMaxWidth()//.height(40.dp)
                        .background(Color.Transparent).onKeyEvent {
                            if (it.key == Key.Enter){
                                isEditMode.value = false
                                true
                            }
                            if (it.key == Key.Escape) {
                                isEditLocal.value = false
                                isEditMode.value = false
                                true
                            }
                            false
                        }.focusRequester(focusRequesterText),
                    value = saldoStrokeName.value ?: "",
                    colors = if (isIncome) colorFieldsDebit else colorFieldsCredit,
                    onValueChange = {
                        if (it.isNotEmpty()) {
                            saldoStrokeName.value = it
                            //newCellSaldo.value.name = it
                            //isEditByHuman.value = true
                        }
                    },
                    label = { Text("Name", color = if (isIncome) colorTextDebitTitle else colorTextCreditTitle, fontSize = 8.sp) },
                    textStyle = TextStyle.Default.copy(fontSize = 20.sp, color = if (isIncome) colorTextDebitTitle else colorTextCreditTitle),
                    keyboardActions = KeyboardActions(
                        onDone = { actionToSaveChanges() },
                        onSearch = { actionToSaveChanges() },
                        onGo = { actionToSaveChanges() },
                        onNext = { actionToSaveChanges() },
                        onSend = { actionToSaveChanges() }
                    )
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Row(Modifier.clickable {
                        deleteCell(monthIndex = parentIndex, saldoCell = saldoCell, isIncome = isIncome)
                        isEditMode.value = false
                        //isEdit.value = false

                    }) {
                        Text("❌", fontSize = 20.sp)
                    }
                    Row(Modifier.clickable {
                        deleteCell(monthIndex = parentIndex, saldoCell = saldoCell, isIncome = isIncome, andFuture = true)
                        //isEdit.value = false
                        isEditMode.value = false
                    }) {
                        Text("❌\uD83D\uDD1C", fontSize = 20.sp)
                    }
                }

            } else {
                val text1 = buildAnnotatedString {
                    withStyle(SpanStyle(color = if (isIncome) colorDebitResult else colorCreditResult, fontWeight = FontWeight.Bold)) {
                        append("${saldoCell.amount}".currency())
                    }
                    withStyle(SpanStyle(color = Color.Gray)) {
                        append("\n" + "${saldoCell.name}")
                    }
                }
                Row(
                    Modifier.fillMaxWidth()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {},
                            onDoubleTap = {
                            },
                            onLongPress = {
                                //detailShow.value = !detailShow.value
                            },
                            onTap = {
                                GlobalScope.launch {
                                    isEditMode.value = true
                                    isEditLocal.value = true
                                    //delay(100)
                                }
                            }
                        )
                    }, horizontalArrangement = Arrangement.SpaceBetween
                ) {
//                    if (!detailShow.value) {
//                        Text(modifier = Modifier.padding(start = 5.dp), text = "${saldoCell.amount} ")
//                        Text(modifier = Modifier.padding(start = 5.dp, end = 5.dp), text ="${if (saldoCell.isConst) "\uD83D\uDD04" else ""}")
//                    } else {
//                                            }
                    if (showTipsInternal.value) {
                        Text(modifier = Modifier.padding(start = 5.dp), text = if(!isIncome) "Your stroke of expense (e.g. buying laptop)" else "Your stroke of income (e.g. salary)", fontStyle = FontStyle.Italic)
                    }else {
                        Text(modifier = Modifier.padding(start = 5.dp), text =text1)
                        Text(modifier = Modifier.padding(start = 5.dp, end = 5.dp), text ="${if (saldoCell.isConst) "\uD83D\uDD04" else ""}")

                    }

                }
            }
        }
    }
}