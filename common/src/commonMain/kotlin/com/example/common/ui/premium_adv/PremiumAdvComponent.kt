package com.example.common.ui.premium_adv

import com.arkivanov.decompose.ComponentContext
import com.example.common.ui.main_dashboard.stateFall
import com.example.common.utils.MainState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

interface IPremiumAdvComponent {
    val uiState: StateFlow<MainState?>

    fun getAllCharacters(name: String = "Moscow")
    fun onCloseClicked()
    fun showSelectedBudget()
}

class AnotherTestComponent(
    componentContext: ComponentContext,
    //private val useCase: GetWeatherByCityUseCase,
    private val onBack: () -> Unit,
    val goToBudget: () -> Unit
) : IPremiumAdvComponent, ComponentContext by componentContext {

    private val _uiState: MutableStateFlow<MainState?> = MutableStateFlow(MainState())
    override val uiState: StateFlow<MainState?> get() = _uiState
    override fun getAllCharacters(name: String) {
        CoroutineScope(Dispatchers.IO).launch {
//            _uiState.value = _uiState.value?.copy(isLoading = true)
//            try {
//                _uiState.value = _uiState.value?.copy(isLoading = false, success = useCase.invoke(name), error = null)
//            } catch (e: Exception) {
//                _uiState.value = _uiState.value?.copy(error = e.message.toString(), isLoading = false)
//            }
        }
    }
    override fun onCloseClicked() {
        onBack
    }

    override fun showSelectedBudget() {
        stateFall.clear()
        goToBudget.invoke()
    }
}
