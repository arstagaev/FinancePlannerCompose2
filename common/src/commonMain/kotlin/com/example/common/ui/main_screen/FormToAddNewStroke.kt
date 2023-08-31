package com.example.common.ui.main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.colorText
import com.example.common.colorTextSecondary
import com.example.common.models.SaldoCell

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun plusik(isIncome: Boolean = true, parentIndex: Int) {
    var saldoStrokeAmount = remember { mutableStateOf("") }
    var saldoStrokeName = remember { mutableStateOf("") }
    val checkedState = remember { mutableStateOf(false) }

    var isEdit = remember { mutableStateOf(false) }
    var newCellSaldo = remember { mutableStateOf<SaldoCell>(SaldoCell(amount = 0, name = "")) }


    LaunchedEffect(isEditMode.value) {
        if (!isEditMode.value && saldoStrokeAmount.value.toString().isNotEmpty() && saldoStrokeAmount.value.toString().isNotBlank()) {
            if (isEdit.value) {
                val newValue = saldoStrokeAmount.value.toInt() ?: 0 // newCellSaldo.value.amount.toInt()
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
                val colorFields = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent, textColor = colorText,
                    focusedLabelColor = colorTextSecondary, unfocusedLabelColor = colorTextSecondary,
                    placeholderColor = colorTextSecondary, unfocusedIndicatorColor = Color.Green, focusedIndicatorColor = colorText
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
                    //value = newCellSaldo.value.amount.toString(),
                    value = saldoStrokeAmount.value,
                    colors = colorFields,
                    onValueChange = {
                        val newNum = it.filter { it.isDigit() }
                        if (newNum.isNotEmpty()) {
                            saldoStrokeAmount.value = newNum
                        }
                    },
                    label = { Text("Amount", color = colorText, fontSize = 8.sp) },
                    textStyle = TextStyle.Default.copy(fontSize = 20.sp),
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
                            if (it.key == Key.Enter) {
                                actionToSaveChanges()
                                true
                            }
                            if (it.key == Key.Escape) {
                                isEditMode.value = false
                                true
                            }
                            false
                        },
                    value = saldoStrokeName.value ?: "",
                    colors = colorFields,
                    onValueChange = {
                        if (it.isNotEmpty()) {
                            saldoStrokeName.value = it
                            //newCellSaldo.value.name = it
                            //isEditByHuman.value = true
                        }
                    },
                    label = { Text("Name", color = colorText, fontSize = 8.sp) },
                    textStyle = TextStyle.Default.copy(fontSize = 20.sp, color = colorText),
                    keyboardActions = KeyboardActions(
                        onDone = { actionToSaveChanges() },
                        onSearch = { actionToSaveChanges() },
                        onGo = {actionToSaveChanges()},
                        onNext = {actionToSaveChanges()},
                        onSend = {actionToSaveChanges()}
                    )
                )
                Spacer(Modifier.fillMaxWidth().height(3.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = checkedState.value,
                        onCheckedChange = {
                            checkedState.value = it
                            //isEditByHuman.value = true
                        },
                        colors = CheckboxDefaults.colors(checkedColor = colorTextSecondary, uncheckedColor = colorTextSecondary, checkmarkColor = Color.Green),
                        modifier = Modifier//.height(40.dp)
                        ,
                    )
                    Text(modifier = Modifier//.height(40.dp)
                        , text = "permanent ${if(isIncome) "income" else "expense"}", color = colorText, fontSize = 12.sp)
                }
            }

        } else {
            Text(text = "+", style = MaterialTheme.typography.body1, color = colorText,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}