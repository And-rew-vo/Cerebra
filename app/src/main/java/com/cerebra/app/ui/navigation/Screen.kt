package com.cerebra.app.ui.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Library : Screen("library")
    object Profile : Screen("profile")
    object Training : Screen("training/{textId}") { // Pass text ID
        fun createRoute(textId: Int) = "training/$textId"
    }
}
