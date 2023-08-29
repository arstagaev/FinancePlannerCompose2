//package com.example.common.utils
//
//import androidx.compose.foundation.layout.padding
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.example.common.ui.main_screen.positionTips
//
//sealed class PositionOfTips(open val modifier: Modifier = Modifier.padding(start = 150.dp, top = 10.dp)) {
//    data class MONTH(           override val modifier: Modifier = Modifier.padding(start = 170.dp, top = 15.dp)): PositionOfTips()
//    data class SALDO_STROKE(    override val modifier: Modifier = Modifier.padding(start = 170.dp, top = 20.dp)): PositionOfTips()
//    data class ADD_STROKE(      override val modifier: Modifier = Modifier.padding(start = 170.dp, top = 20.dp)): PositionOfTips()
//    data class SUM_INCOME(      override val modifier: Modifier = Modifier.padding(start = 170.dp, top = 20.dp)): PositionOfTips()
//    data class CUMULATIVE_SUM(  override val modifier: Modifier = Modifier.padding(start = 170.dp, top = 20.dp)): PositionOfTips()
//    data class INITIAL_SUM(     override val modifier: Modifier = Modifier.padding(start = 170.dp, top = 20.dp)): PositionOfTips()
//    data class SUM_EXPENSE(     override val modifier: Modifier = Modifier.padding(start = 170.dp, top = 20.dp)): PositionOfTips()
//    data class SAME_THINGS_WITH_EXPENSE(override val modifier: Modifier = Modifier.padding(start = 170.dp, top = 20.dp)): PositionOfTips()
//    data class CAN_EDIT_STROKE(         override val modifier: Modifier = Modifier.padding(start = 170.dp, top = 20.dp)): PositionOfTips()
//    data class CAN_ADD_MONTH(           override val modifier: Modifier = Modifier.padding(start = 170.dp, top = 20.dp)): PositionOfTips()
//}
//
//fun changePositionOfTips() {
//    when(positionTips.value) {
//        is PositionOfTips.MONTH -> {
//            positionTips.value = PositionOfTips.SALDO_STROKE()
//        }
//        is PositionOfTips.SALDO_STROKE -> {
//            positionTips.value = PositionOfTips.ADD_STROKE()
//        }
//        is PositionOfTips.ADD_STROKE -> {
//            positionTips.value = PositionOfTips.SUM_INCOME()
//        }
//        is PositionOfTips.SUM_INCOME -> {
//            positionTips.value = PositionOfTips.CUMULATIVE_SUM()
//        }
//        is PositionOfTips.CUMULATIVE_SUM -> {
//            positionTips.value = PositionOfTips.INITIAL_SUM()
//        }
//        is PositionOfTips.INITIAL_SUM -> {
//            positionTips.value = PositionOfTips.SUM_EXPENSE()
//        }
//        is PositionOfTips.SUM_EXPENSE -> {
//            positionTips.value = PositionOfTips.SAME_THINGS_WITH_EXPENSE()
//        }
//        is PositionOfTips.SAME_THINGS_WITH_EXPENSE -> {
//            positionTips.value = PositionOfTips.CAN_EDIT_STROKE()
//        }
//        is PositionOfTips.CAN_EDIT_STROKE -> {
//            positionTips.value = PositionOfTips.CAN_ADD_MONTH()
//        }
//        is PositionOfTips.CAN_ADD_MONTH -> {
//            positionTips.value = PositionOfTips.SALDO_STROKE()
//        }
//    }
//}