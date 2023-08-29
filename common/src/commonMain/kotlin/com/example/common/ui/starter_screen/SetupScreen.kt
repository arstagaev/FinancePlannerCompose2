package com.example.common.ui.starter_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.common.ui.list_saldos_screen.ListOfBudgets
import com.example.common.ui.main_screen.BudgetScreen
import com.example.common.ui.main_screen.stateFall
import com.example.common.ui.main_screen.updateWhole


internal var showBudget = mutableStateOf(false)

@Composable
fun StarterScreen() {
    var showBudgetInternal = remember { showBudget }

    if (showBudgetInternal.value) {
        //initital()
        updateWhole()
        BudgetScreen()
    } else {
        stateFall.clear()
        ListOfBudgets()
    }
}