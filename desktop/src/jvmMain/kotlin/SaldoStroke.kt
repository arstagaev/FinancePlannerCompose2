data class SaldoStroke(
    val id: Long,
    var amount: Int = 0,
    var month: Int,
    var year: Int,
    var nature: Nature,
    var isEdit: Boolean = false,
    var isConst: Boolean = false
)
