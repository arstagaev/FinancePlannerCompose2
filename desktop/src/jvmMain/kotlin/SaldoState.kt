data class SaldoState(
    var saldoAction: SaldoAction = SaldoAction.SHOW
)

enum class SaldoAction {
    EDITING,
    DONE_EDIT,
    REFRESH,
    SHOW
}

