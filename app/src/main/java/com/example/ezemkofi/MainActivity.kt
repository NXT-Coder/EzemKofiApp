package com.example.ezemkofi

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ezemkofi.navigation.AppNavigation
import com.example.ezemkofi.ui.theme.CartViewModel
import com.example.ezemkofi.ui.theme.CoffeeListViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = CoffeeListViewModel()
        val context = this
        val cartViewModel = CartViewModel(context = context)
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        setContent {
            AppNavigation(sharedPreferences, context, cartViewModel = cartViewModel, viewModel = viewModel)
        }
    }
}