package com.moonfly.gamizer.shared

import com.moonfly.gamizer.BuildConfig

actual fun apiKey(): String {
    return BuildConfig.API_KEY
}