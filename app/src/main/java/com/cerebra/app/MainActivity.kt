package com.cerebra.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.cerebra.app.ui.navigation.CerebraNavGraph
import com.cerebra.app.ui.theme.CerebraTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CerebraTheme {
                CerebraNavGraph()
            }
        }
    }
}
