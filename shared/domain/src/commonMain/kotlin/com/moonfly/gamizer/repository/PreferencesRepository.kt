package com.moonfly.gamizer.repository

class PreferencesRepository(private val userPreferencesDataSource: UserPreferencesDataSource) {

    fun getPreferences() = userPreferencesDataSource.getPreferences()

    suspend fun changeDarkMode(isOn: Boolean) = userPreferencesDataSource.changeDarkMode(isOn)

}