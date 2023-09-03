package com.example.common.ui.root

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.example.common.ui.premium_adv.ListOfBudgets
import com.example.common.ui.main_dashboard.BudgetScreen

@Composable
fun RootContent(
    component: IRootComponent,
    modifier: Modifier = Modifier
) {

    val childStack by component.childStack.subscribeAsState()
    val activeComponent = childStack.active.instance

    Column(Modifier.fillMaxWidth()) {
        Children(
            stack = component.childStack,
            animation = stackAnimation(fade()),
            modifier = Modifier.weight(weight = 1F)
        ) {
            when (val child = it.instance) {
                is IRootComponent.Child.MainDashboardChild -> BudgetScreen(child.component)
                is IRootComponent.Child.PremiumAdvChild -> ListOfBudgets(child.component)
            }
        }
    }
}
