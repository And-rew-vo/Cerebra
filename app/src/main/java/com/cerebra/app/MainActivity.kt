package com.cerebra.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.cerebra.app.ui.navigation.CerebraNavGraph
import com.cerebra.app.ui.theme.CerebraTheme
import dagger.hilt.android.AndroidEntryPoint

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.cerebra.app.ui.MainViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val isDark by viewModel.isDarkMode.collectAsState()
            
            CerebraTheme(darkTheme = isDark) {
                CerebraNavGraph()
            }
        }
    }
}
