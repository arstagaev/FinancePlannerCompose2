

data class Saldo(
    var month: Int,
    var year: Int,
    var wholeStrokes: ArrayList<SaldoStroke>
)

enum class Nature { DEBIT, CREDIT }
