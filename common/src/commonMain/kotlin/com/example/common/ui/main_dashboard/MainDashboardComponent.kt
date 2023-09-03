package com.example.common.ui.main_dashboard

import com.arkivanov.decompose.ComponentContext

interface IMainDashboardComponent {

    // Omitted code

    fun onItemClicked(id: Long)

    fun toBackListOfBudgets()
}

class MainDashboardComponent(
    componentContext: ComponentContext,
    private val onItemSelected: (id: Long) -> Unit,
    private val backToBudgets: () -> Unit
) : IMainDashboardComponent, ComponentContext by componentContext {
    init {
        updateWhole()
    }
    // Omitted code

    override fun onItemClicked(id: Long) {
        onItemSelected(id)
    }

    override fun toBackListOfBudgets() {
        backToBudgets.invoke()
    }
}