package com.moonfly.gamizer.base

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.moonfly.gamizer.gamedetails.GameDetailsMainView
import com.moonfly.gamizer.gamedetails.GameDetailsViewModel
import com.moonfly.gamizer.gamelist.GameListMainView
import com.moonfly.gamizer.navigation.Screen
import com.moonfly.gamizer.navigation.NavigationItem
import com.moonfly.gamizer.navigation.correspondingBottomNav
import com.moonfly.gamizer.preferences.PreferencesMainView
import gamizer.shared.presentation.generated.resources.Res
import gamizer.shared.presentation.generated.resources.generic_error_message
import gamizer.shared.presentation.generated.resources.refresh
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun LoadingBar() {
    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
            )
        }
    }
}

@Composable
fun ErrorMessage(onRefreshClick: () -> Unit) {
    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(stringResource(Res.string.generic_error_message))
                Button(onClick = onRefreshClick) {
                    Text(stringResource(Res.string.refresh))
                }
            }
        }
    }
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }
    val typography = Typography(
        bodyMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
    )
    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp)
    )

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}

@Composable
fun RootView() {
    val viewModel: MainViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    if (!state.isLoading) {
        MyApplicationTheme(darkTheme = state.darkMode) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                GamizerApp()
            }
        }
    }
}


@Composable
fun GamizerApp() {
    val navController = rememberNavController()
    val backstackEntry by navController.currentBackStackEntryAsState()

    Scaffold(bottomBar = {
        BottomNavigationBar(
            items = NavigationItem.entries,
            currentDestination = backstackEntry?.destination,
            onItemClick = {
                navController.navigate(it.route) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            })
    }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.List.title,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = Screen.List.title) {
                GameListMainView {
                    navController.navigate("${Screen.Details.title}/$it")
                }
            }
            composable("${Screen.Details.title}/{${GameDetailsViewModel.GAME_ID_KEY}}",
                arguments = listOf(
                    navArgument(GameDetailsViewModel.GAME_ID_KEY) { type = NavType.IntType }
                )) {
                GameDetailsMainView()
            }
            composable(route = Screen.Preferences.title) {
                PreferencesMainView()
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    items: List<NavigationItem>,
    currentDestination: NavDestination?,
    onItemClick: (NavigationItem) -> Unit
) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
    ) {
        items.forEach { navigationItem ->
            val isSelected = Screen.entries.firstOrNull { currentDestination?.route?.contains(it.title) == true }?.correspondingBottomNav() == navigationItem
            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemClick(navigationItem) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) navigationItem.selectedIcon else navigationItem.unSelectedIcon,
                        contentDescription = stringResource(navigationItem.title),
                    )
                },
                label = {
                    Text(
                        text = stringResource(navigationItem.title),
                        style = if (isSelected) MaterialTheme.typography.labelLarge
                        else MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
            )
        }
    }
}