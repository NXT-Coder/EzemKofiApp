package com.example.ezemkofi.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.ezemkofi.R
import com.example.ezemkofi.components.CoffeeDetail
import com.example.ezemkofi.ui.theme.CartViewModel
import com.example.ezemkofi.ui.theme.CoffeeListViewModel

@Composable
fun CoffeeDetailScreen(
    coffeeId: String,
    viewModel: CoffeeListViewModel,
    cartViewModel: CartViewModel,
    navController: NavHostController,
    context: Context
) {
    val coffeeDetails by viewModel.coffeeDetails.observeAsState()

    LaunchedEffect(coffeeId) {
        viewModel.fetchCoffeeDetails(coffeeId)
    }

    Surface(
        modifier = Modifier
            .background(Color.White)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            coffeeDetails?.let { coffee ->
                CoffeeDetail(
                    context = context,
                    text = stringResource(id = R.string.addToCart),
                    coffee = coffee,
                    viewModel = viewModel,
                    navController = navController,
                    cartViewModel = cartViewModel
                )
            }
        }
    }
}