package com.example.common

import com.example.common.utils.Platform

actual fun getPlatformName(): Platform {
    return Platform.ANDROID
}