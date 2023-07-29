import Consts._currentBudget
import kotlinx.coroutines.flow.MutableSharedFlow


fun refresh(saldoStrokeInp: SaldoStroke, month: Int, year: Int) {

    _currentBudget.value.map {
        it.wholeStrokes.forEachIndexed { saldoStrkIndex, saldoStrk ->
            if (saldoStrk.id == saldoStrokeInp.id) {
                //_currentBudget.value[].copy(currentBudget.value.get(saldoStrkIndex).copy(amount = 666))

            }
        }
    }
    _currentBudget.value.forEachIndexed { index, saldo ->
        saldo.wholeStrokes.forEachIndexed { saldoStrkIndex, saldoStrk ->
            if (saldoStrk.id == saldoStrokeInp.id) {

                //_currentBudget.value = _currentBudget.value[index].wholeStrokes[saldoStrkIndex].copy(amount = _currentBudget.value[saldoStrkIndex].wholeStrokes[saldoStrkIndex].copy(amount = 666))
                //_currentBudget.value = _currentBudget.value[saldoStrkIndex].wholeStrokes[saldoStrkIndex].copy(amount = 666)

            }
        }
    }
    println("<>>>>>>${ _currentBudget.value.joinToString()}")
    //println("<>>>>>>${_currentBudget.value.joinToString()}")
    //_currentBudget.value = _currentBudget.value[0].credit.get(1).
    //saldoStroke
}
