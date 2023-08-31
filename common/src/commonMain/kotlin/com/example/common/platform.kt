package com.example.common

import com.example.common.utils.Platform

expect fun getPlatformName(): Platform

expect fun createFiveSlots()

expect suspend fun saveNewBudgetJSON()

expect suspend fun refreshBudgetJSON()

expect suspend fun decodeFromFile()

expect suspend fun getListOfBudgets(): ArrayList<String>