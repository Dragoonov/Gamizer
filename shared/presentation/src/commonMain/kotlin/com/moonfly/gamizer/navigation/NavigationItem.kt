package com.moonfly.gamizer.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import gamizer.shared.presentation.generated.resources.Res
import gamizer.shared.presentation.generated.resources.home_bottom_navbar
import gamizer.shared.presentation.generated.resources.preferences_bottom_navbar
import org.jetbrains.compose.resources.StringResource

enum class NavigationItem(
    val unSelectedIcon: ImageVector,
    val selectedIcon: ImageVector,
    val title: StringResource,
    val route: String
) {
    Home(
        unSelectedIcon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home,
        title = Res.string.home_bottom_navbar,
        route = Screen.List.title,
    ),
    Preferences(
        unSelectedIcon = Icons.Outlined.Settings,
        selectedIcon = Icons.Filled.Settings,
        title = Res.string.preferences_bottom_navbar,
        route = Screen.Preferences.title,
    )
}

fun Screen.correspondingBottomNav(): NavigationItem {
    return when(this) {
        Screen.List, Screen.Details -> NavigationItem.Home
        Screen.Preferences -> NavigationItem.Preferences
    }
}