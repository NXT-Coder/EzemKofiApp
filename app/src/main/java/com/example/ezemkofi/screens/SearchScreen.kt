package com.example.ezemkofi.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ezemkofi.components.CoffeeSearch
import com.example.ezemkofi.ui.theme.CoffeeListViewModel

@Composable
fun SearchScreen(
    context: Context,
    coffeeListViewModel: CoffeeListViewModel = viewModel(),
    navController: NavHostController
) {
    Surface(
        modifier = Modifier
            .background(Color.White)
            .heightIn(35.dp),
        color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()){
            Column {
                CoffeeSearch(
                    context = context,
                    viewModel = coffeeListViewModel,
                    navController = navController
                )
            }
        }
    }
}