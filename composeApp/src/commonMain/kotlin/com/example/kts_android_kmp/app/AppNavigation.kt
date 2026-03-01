package com.example.kts_android_kmp.app

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kts_android_kmp.feature.intro.HelloScreen
import com.example.kts_android_kmp.feature.login.LoginScreen
import com.example.kts_android_kmp.feature.login.LoginViewModel
import io.github.aakira.napier.Napier

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel { LoginViewModel() }

    NavHost(
        navController = navController,
        startDestination = Screen.Hello.route
    ) {
        composable(Screen.Hello.route) {
            HelloScreen(
                onLoginButtonClick = { navController.navigate(Screen.Login.route) },
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                loginViewModel = loginViewModel,
                onLogin = { email, password ->
                    run {
                        Napier.w("email: $email, password: $password")
                    }
                }
            )
        }
    }
}
