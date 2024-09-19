package com.moonfly.gamizer.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.moonfly.gamizer.gamedetails.GameDetailsMainView
import com.moonfly.gamizer.gamedetails.GameDetailsViewModel
import com.moonfly.gamizer.gamelist.GameListMainView
import com.moonfly.gamizer.navigation.GamizerScreen
import gamizer.shared.presentation.generated.resources.Res
import gamizer.shared.presentation.generated.resources.generic_error_message
import gamizer.shared.presentation.generated.resources.refresh
import org.jetbrains.compose.resources.stringResource


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
fun GamizerApp() {
    val navController = rememberNavController()
    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = GamizerScreen.List.title,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = GamizerScreen.List.title) {
                GameListMainView {
                    navController.navigate("${GamizerScreen.Details.title}/$it")
                }
            }
            composable("${GamizerScreen.Details.title}/{${GameDetailsViewModel.GAME_ID_KEY}}",
                arguments = listOf(
                    navArgument(GameDetailsViewModel.GAME_ID_KEY) { type = NavType.IntType }
                )) {
                GameDetailsMainView()
            }
        }
    }
}