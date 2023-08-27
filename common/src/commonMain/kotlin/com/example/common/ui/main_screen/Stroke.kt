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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.colorText
import com.example.common.colorTextSecondary
import com.example.common.models.SaldoCell
import com.example.common.utils.currency
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.awt.event.KeyEvent

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
            Modifier.fillMaxSize()//.width(100.dp)
                .background(if (isIncome) colorDebitStroke else colorCreditStroke)
        ) {
            if (isEdit.value) {

                TextField(
                    modifier = Modifier.fillMaxWidth()//.height(40.dp)
                        .background(Color.Transparent).onKeyEvent {
                            if (it.key == Key.Enter) {
                                actionToSaveChanges()
                                true
                            }
                            false
                        },
                    //value = newCellSaldo.value.amount.toString(),
                    value = saldoStrokeAmount.value,
                    colors = if (isIncome) colorFieldsDebit else colorFieldsCredit,
                    onValueChange = { newStroke ->
                        val newNum = newStroke.filter { it.isDigit() || it == '-' }
                        if (newNum.isNotEmpty()) {
                            saldoStrokeAmount.value = newNum
                        }
                    },
                    label = { Text("Amount", color = if (isIncome) colorTextDebitTitle else colorTextCreditTitle, fontSize = 8.sp) },
                    textStyle = TextStyle.Default.copy(fontSize = 20.sp, color = if (isIncome) colorTextDebitTitle else colorTextCreditTitle, fontWeight = FontWeight.Bold),
                    keyboardActions = KeyboardActions(
                        onDone = { actionToSaveChanges() },
                        onSearch = { actionToSaveChanges() },
                        onGo = {actionToSaveChanges()},
                        onNext = {actionToSaveChanges()},
                        onSend = {actionToSaveChanges()}
                    )
                )
                TextField(
                    modifier = Modifier.fillMaxWidth()//.height(40.dp)
                        .background(Color.Transparent).onKeyEvent {
                            if (it.key == Key.Enter){
                                actionToSaveChanges()
                                true
                            }
                            false
                        },
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
                        onGo = {actionToSaveChanges()},
                        onNext = {actionToSaveChanges()},
                        onSend = {actionToSaveChanges()}
                    )
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
                        append("${saldoCell.amount}".currency())
                    }
                    withStyle(SpanStyle(color = Color.LightGray)) {
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