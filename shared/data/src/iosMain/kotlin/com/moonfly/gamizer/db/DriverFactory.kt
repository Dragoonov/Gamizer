package com.moonfly.gamizer.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.moonfly.gamizer.GamizerDB

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(GamizerDB.Schema, NAME)
    }
}