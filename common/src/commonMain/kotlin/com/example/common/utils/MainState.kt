package com.example.common.utils



data class MainState (
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean? = null
)