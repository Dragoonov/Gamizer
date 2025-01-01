package com.moonfly.gamizer.preferences

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.moonfly.gamizer.base.ErrorMessage
import com.moonfly.gamizer.base.LoadingBar
import com.moonfly.gamizer.base.extraLargeSpace
import com.moonfly.gamizer.base.largeFont
import gamizer.shared.presentation.generated.resources.Res
import gamizer.shared.presentation.generated.resources.dark_mode
import gamizer.shared.presentation.generated.resources.preferences_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PreferencesMainView(viewModel: PreferencesViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Box {
        when {
            !uiState.isError && !uiState.isLoading ->
                Column(modifier = Modifier.fillMaxSize().padding(extraLargeSpace)) {
                    Text(text = stringResource(Res.string.preferences_title), fontSize = largeFont, fontWeight = FontWeight.Bold)
                    DarkModeSwitch(uiState.darkMode) {
                        viewModel.handleEvent(PreferencesEvent.OnDarkModeChanged(it))
                    }
                }
            uiState.isError -> ErrorMessage {
                viewModel.handleEvent(PreferencesEvent.OnRefresh)
            }

            else -> LoadingBar()
        }

    }
}

@Composable
fun DarkModeSwitch(isOn: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(stringResource(Res.string.dark_mode))
        Switch(
            checked = isOn,
            onCheckedChange = onCheckedChange
        )
    }
}