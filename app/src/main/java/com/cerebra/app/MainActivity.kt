package com.cerebra.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.cerebra.app.ui.navigation.CerebraNavGraph
import com.cerebra.app.ui.navigation.Screen
import com.cerebra.app.ui.theme.CerebraTheme
import com.cerebra.app.ui.MainUiState
import dagger.hilt.android.AndroidEntryPoint

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.fillMaxSize
import com.cerebra.app.ui.MainViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            
            CerebraTheme(darkTheme = uiState.isDarkMode) {
                when (val state = uiState) {
                    is MainUiState.Loading -> {
                        androidx.compose.foundation.layout.Box(
                            modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            androidx.compose.material3.CircularProgressIndicator()
                        }
                    }
                    is MainUiState.Authenticated -> {
                        CerebraNavGraph(startDestination = Screen.Home.route)
                    }
                    is MainUiState.Unauthenticated -> {
                        CerebraNavGraph(startDestination = Screen.Welcome.route)
                    }
                }
            }
        }
    }
}
