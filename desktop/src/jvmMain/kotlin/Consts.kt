import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object Consts {

//    val _currentBudget = MutableStateFlow(arrayListOf<SaldoStroke>(
//        SaldoStroke(id = randomId(), nature = Nature.DEBIT,  amount = 666, year = 2023, month = 11),
//        SaldoStroke(id = randomId(), nature = Nature.DEBIT,  amount = 100, year = 2023, month = 11),
//        SaldoStroke(id = randomId(), nature = Nature.DEBIT,  amount = 100, year = 2023, month = 12),
//        SaldoStroke(id = randomId(), nature = Nature.DEBIT,  amount = 100, year = 2023, month = 12),
//        SaldoStroke(id = randomId(), nature = Nature.CREDIT, amount = 100, year = 2023, month = 12),
//        SaldoStroke(id = randomId(), nature = Nature.CREDIT, amount = 100, year = 2023, month = 12),
//
//        SaldoStroke(id = randomId(), nature = Nature.DEBIT,  amount = 101, year = 2023, month = 11),
//        SaldoStroke(id = randomId(), nature = Nature.DEBIT,  amount = 100, year = 2023, month = 11),
//        SaldoStroke(id = randomId(), nature = Nature.DEBIT,  amount = 100, year = 2023, month = 12),
//        SaldoStroke(id = randomId(), nature = Nature.DEBIT,  amount = 100, year = 2023, month = 12),
//        SaldoStroke(id = randomId(), nature = Nature.CREDIT, amount = 100, year = 2023, month = 12),
//        SaldoStroke(id = randomId(), nature = Nature.CREDIT, amount = 100, year = 2023, month = 12),
//
//        SaldoStroke(id = randomId(), nature = Nature.DEBIT,  amount = 101, year = 2023, month = 11),
//        SaldoStroke(id = randomId(), nature = Nature.DEBIT,  amount = 100, year = 2023, month = 11),
//        SaldoStroke(id = randomId(), nature = Nature.DEBIT,  amount = 100, year = 2023, month = 12),
//        SaldoStroke(id = randomId(), nature = Nature.DEBIT,  amount = 100, year = 2023, month = 12),
//        SaldoStroke(id = randomId(), nature = Nature.CREDIT, amount = 100, year = 2023, month = 12),
//        SaldoStroke(id = randomId(), nature = Nature.CREDIT, amount = 777, year = 2023, month = 12),
//    ))

    val _currentBudget = MutableStateFlow(arrayListOf<Saldo>(
        Saldo(
            month = 11,
            year = 2023,
            wholeStrokes = arrayListOf(
                SaldoStroke(randomId(), amount = 111, month=12, year= 2023, nature = Nature.DEBIT),
                SaldoStroke(randomId(), amount = 100, month=12, year= 2023, nature = Nature.DEBIT),
                SaldoStroke(randomId(), amount = 100, month=12, year= 2023, nature = Nature.DEBIT),
                SaldoStroke(randomId(), amount = 100, month=12, year= 2023, nature = Nature.CREDIT),
                SaldoStroke(randomId(), amount = 100, month=12, year= 2023, nature = Nature.CREDIT),
            )
        ),
        Saldo(
            month = 12,
            year = 2023,
            wholeStrokes = arrayListOf(
                SaldoStroke(randomId(), amount = 100, month=12, year= 2023, nature = Nature.DEBIT),
                SaldoStroke(randomId(), amount = 100, month=12, year= 2023, nature = Nature.DEBIT),
                SaldoStroke(randomId(), amount = 100, month=12, year= 2023, nature = Nature.DEBIT),
                SaldoStroke(randomId(), amount = 100, month=12, year= 2023, nature = Nature.CREDIT),
                SaldoStroke(randomId(), amount = 666, month=12, year= 2023, nature = Nature.CREDIT),
            )
        )
    ))
    val currentBudget: StateFlow<ArrayList<Saldo>> get() =  _currentBudget

    //val isEditNow = mutableStateOf(false)
    val saldoState = mutableStateOf<SaldoState>(SaldoState(SaldoAction.SHOW))
    val actionPerforming = mutableStateOf<SaldoState>(SaldoState(SaldoAction.SHOW))

    var indxEdit = 0
}