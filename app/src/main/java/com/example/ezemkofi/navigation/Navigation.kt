package com.example.ezemkofi.navigation

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ezemkofi.screens.CartScreen
import com.example.ezemkofi.screens.CoffeeDetailScreen
import com.example.ezemkofi.screens.LoginScreen
import com.example.ezemkofi.screens.MainScreen
import com.example.ezemkofi.screens.SearchScreen
import com.example.ezemkofi.screens.SignUpScreen
import com.example.ezemkofi.ui.theme.CartViewModel
import com.example.ezemkofi.ui.theme.CoffeeListViewModel

@Composable
fun AppNavigation(
    sharedPreferences: SharedPreferences,
    context: Context,
    viewModel: CoffeeListViewModel,
    cartViewModel: CartViewModel
){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login"){
        composable("login"){
            LoginScreen(navController = navController, sharedPreferences)
        }

        composable("signUp"){
            SignUpScreen(navController = navController)
        }

        composable("main/{userName}", arguments = listOf(navArgument("userName"){type = NavType.StringType})){navBackStackEntry ->
            val userName = navBackStackEntry.arguments?.getString("userName") ?: ""
            MainScreen(fullName = userName, navController = navController)
        }

        composable("coffeeDetail/{coffeeId}") { backStackEntry ->
            val coffeeId = backStackEntry.arguments?.getString("coffeeId") ?: return@composable
            CoffeeDetailScreen(
                coffeeId = coffeeId,
                viewModel = viewModel,
                navController = navController,
                context = context,
                cartViewModel = cartViewModel
            )
        }

        composable("search"){
            SearchScreen(context = context, navController = navController)
        }

        composable("cart"){
            CartScreen(
                cartViewModel = cartViewModel,
                navController = navController,
                sharedPreferences = sharedPreferences
            )
        }
    }
}