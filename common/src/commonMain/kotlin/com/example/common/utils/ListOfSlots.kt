package com.example.common.utils

sealed class ListOfSlots(open val fileName: String = "first_slot") {

    data class FIRST(override var fileName : String = "first_slot"): ListOfSlots()
    data class SECOND(override var fileName: String = "second_slot"): ListOfSlots()
    data class THIRD(override var fileName : String = "third_slot"): ListOfSlots()
    data class FOURTH(override var fileName: String = "fourth_slot"): ListOfSlots()
    data class FIFTH(override var fileName : String = "fifth_slot"): ListOfSlots()

//    companion object {
//        const val FIRST = "first.json"
//        const val SECOND = ("second.json")
//        const val THIRD = ("third.json")
//        const val FOURTH = ("fourth.json")
//        const val FIFTH = ("fifth.json")
//    }
}