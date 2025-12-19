package com.cerebra.app.ui.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Library : Screen("library")
    object Profile : Screen("profile")
    object Training : Screen("training/{textId}") {
        fun createRoute(textId: Int) = "training/$textId"
    }
    object AddText : Screen("add_text?textId={textId}") {
        fun createRoute(textId: Int? = null) = if (textId != null) "add_text?textId=$textId" else "add_text"
    }
    object CommonTextDetail : Screen("detail/{textId}") {
        fun createRoute(textId: String) = "detail/$textId"
    }
}
