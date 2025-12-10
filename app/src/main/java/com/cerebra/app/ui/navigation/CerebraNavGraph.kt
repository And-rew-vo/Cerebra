package com.cerebra.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cerebra.app.ui.auth.AuthViewModel
import com.cerebra.app.ui.auth.LoginScreen
import com.cerebra.app.ui.auth.RegisterScreen
import com.cerebra.app.ui.auth.WelcomeScreen
import com.cerebra.app.ui.main.AddTextScreen
import com.cerebra.app.ui.main.HomeScreen
import com.cerebra.app.ui.main.LibraryScreen
import com.cerebra.app.ui.main.ProfileScreen
import com.cerebra.app.ui.training.TrainingScreen

@Composable
fun CerebraNavGraph(
    startDestination: String,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val authState by authViewModel.uiState.collectAsState()
    
    val bottomNavItems = listOf(
        Screen.Home,
        Screen.Library,
        Screen.Profile
    )
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomNav = currentRoute in listOf(Screen.Home.route, Screen.Library.route, Screen.Profile.route)

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                NavigationBar {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = {
                                when(screen) {
                                    Screen.Home -> Icon(Icons.Default.Home, null)
                                    Screen.Library -> Icon(Icons.Default.List, null)
                                    Screen.Profile -> Icon(Icons.Default.Person, null)
                                    else -> {}
                                }
                            },
                            label = { 
                                when(screen) {
                                    Screen.Home -> Text("Главная")
                                    Screen.Library -> Text("Библиотека")
                                    Screen.Profile -> Text("Профиль")
                                    else -> {}
                                }
                             },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Welcome.route) {
                WelcomeScreen(
                    onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                    onNavigateToRegister = { navController.navigate(Screen.Register.route) }
                )
            }
            
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Welcome.route) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                    viewModel = authViewModel
                )
            }
            
            composable(Screen.Register.route) {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Welcome.route) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                    viewModel = authViewModel
                )
            }
            
            composable(Screen.Home.route) {
                authState.user?.let { user ->
                    HomeScreen(
                        user = user,
                        onNavigateToTraining = { textId -> 
                            navController.navigate(Screen.Training.createRoute(textId))
                        }
                    )
                }
            }
            
            composable(Screen.Library.route) {
                authState.user?.let { _ ->
                    LibraryScreen(
                        onNavigateToTraining = { textId -> 
                            navController.navigate(Screen.Training.createRoute(textId))
                        },
                        onNavigateToAddText = {
                            navController.navigate(Screen.AddText.route)
                        }
                    )
                }
            }
            
            composable(Screen.Profile.route) {
                authState.user?.let { user ->
                    ProfileScreen(
                        user = user,
                        onLogout = {
                            authViewModel.logout()
                            navController.navigate(Screen.Welcome.route) {
                                popUpTo(0)
                            }
                        }
                    )
                }
            }
            
            composable(Screen.AddText.route) {
                AddTextScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            composable(
                route = Screen.Training.route,
                arguments = listOf(navArgument("textId") { type = NavType.IntType })
            ) {
                TrainingScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
