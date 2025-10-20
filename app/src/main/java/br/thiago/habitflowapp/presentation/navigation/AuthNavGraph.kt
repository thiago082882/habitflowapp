package br.thiago.habitflowapp.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.thiago.habitflowapp.presentation.screens.forgotPassword.ForgotPasswordScreen
import br.thiago.habitflowapp.presentation.screens.login.LoginScreen
import br.thiago.habitflowapp.presentation.screens.signUp.RegisterScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthScreen.Login.route
    ) {

        composable(route = AuthScreen.Login.route) {
           LoginScreen(
               onLoginClick = {
                   navController.navigate(Graph.HOME)
               },
               onForgotPasswordClick = {
                   navController.navigate(AuthScreen.Forgot.route)
               },
               onRegisterClick = {
                   navController.navigate(AuthScreen.Signup.route)
               }
           )
        }

        composable(route = AuthScreen.Signup.route) {

            RegisterScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onRegisterClick = {
                    navController.navigate(Graph.HOME)
                },
                onAlreadyHaveAccountClick = {
                    navController.navigate(AuthScreen.Login.route)
                }


            )
        }
        composable(route = AuthScreen.Forgot.route) {
            ForgotPasswordScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSendLinkClick = {
                    navController.navigate(AuthScreen.Login.route)
                }

            )

        }

    }
}

sealed class AuthScreen(val route: String) {

    object Login: AuthScreen("login")
    object Signup: AuthScreen("signup")
    object Forgot: AuthScreen("forgot")

}

