package com.moonfly.gamizer.db

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.moonfly.gamizer.GamizerDB

actual class DriverFactory(private val context: Context) {
  actual fun createDriver(): SqlDriver {
    return AndroidSqliteDriver(GamizerDB.Schema, context, "GamizerDB.db")
  }
}