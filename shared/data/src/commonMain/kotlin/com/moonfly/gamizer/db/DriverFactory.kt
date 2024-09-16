package com.moonfly.gamizer.db

import app.cash.sqldelight.db.SqlDriver
import com.moonfly.gamizer.GamizerDB

expect class DriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): GamizerDB {
    val driver = driverFactory.createDriver()
    return GamizerDB(driver)
}