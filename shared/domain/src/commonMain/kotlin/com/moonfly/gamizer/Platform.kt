package com.moonfly.gamizer

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform