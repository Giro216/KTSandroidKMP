package com.example.kts_android_kmp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    Napier.base(DebugAntilog())

    val modifier = Modifier
        .fillMaxSize()
        .padding(
            start = 20.dp,
            end = 20.dp
        )

    NavHost(
        navController = navController,
        startDestination = Screens.Login.route // TODO вернуть на начальный экран
    ) {
        composable(Screens.HelloScreen.route){
            HelloScreen(
                onLoginButtonClick = {navController.navigate(Screens.Login.route)},
                modifier = modifier)
        }
        composable(Screens.Login.route){
            LoginScreen(
                modifier,
                onLogin = { email, password ->
                    run {
                        Napier.w("email: $email, password: $password")
                    }
                }
            )
        }
    }
}

sealed class Screens(val route: String) {
    object HelloScreen : Screens("Hello_screen")
    object Login : Screens("Login")
}