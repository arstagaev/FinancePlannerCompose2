package com.example.common.utils

sealed class ListOfSlots(open val fileName: String = "first.json") {

    data class FIRST(override var fileName : String = "first.json"): ListOfSlots()
    data class SECOND(override var fileName: String = "second.json"): ListOfSlots()
    data class THIRD(override var fileName : String = "third.json"): ListOfSlots()
    data class FOURTH(override var fileName: String = "fourth.json"): ListOfSlots()
    data class FIFTH(override var fileName : String = "fifth.json"): ListOfSlots()

//    companion object {
//        const val FIRST = "first.json"
//        const val SECOND = ("second.json")
//        const val THIRD = ("third.json")
//        const val FOURTH = ("fourth.json")
//        const val FIFTH = ("fifth.json")
//    }
}