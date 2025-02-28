package com.example.ezemkofi.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ezemkofi.R
import com.example.ezemkofi.components.CoffeeTopPicksSection
import com.example.ezemkofi.components.HeaderBar
import com.example.ezemkofi.components.HeaderText
import com.example.ezemkofi.ui.theme.Coffee
import com.example.ezemkofi.ui.theme.CoffeeListViewModel
import com.example.ezemkofi.ui.theme.LoginViewModel
import com.example.ezemkofi.ui.theme.LoginViewModelFactory

@Composable
fun MainScreen(
    fullName: String,
    navController: NavHostController,
    coffeeListViewModel: CoffeeListViewModel = viewModel()
) {
    val context = LocalContext.current

    val viewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(context)
    )

    LaunchedEffect(Unit) {
        coffeeListViewModel.fetchCoffeeList()
    }

    Surface(
        modifier = Modifier
            .heightIn(35.dp)
            .background(Color.White),
        color = Color.White
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            Column {
                HeaderBar(
                    text = stringResource(id = R.string.mainHeader),
                    name = fullName,
                    category = stringResource(id = R.string.categories),
                    navController = navController,
                    viewModel = viewModel
                )

                HeaderText(value = stringResource(id = R.string.topPicks))

                CoffeeTopPicksSection(viewModel = coffeeListViewModel)
            }
        }
    }
}