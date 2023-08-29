package com.example.common

expect fun getPlatformName(): String

expect fun createFiveSlots()

expect suspend fun saveNewBudgetJSON()

expect suspend fun refreshBudgetJSON()

expect suspend fun decodeFromFile()

expect suspend fun getListOfBudgets(): ArrayList<String>