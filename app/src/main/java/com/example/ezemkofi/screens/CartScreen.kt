package com.example.ezemkofi.screens

import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ezemkofi.R
import com.example.ezemkofi.components.CartBar
import com.example.ezemkofi.components.CoffeeCart
import com.example.ezemkofi.components.poppinsFontFamily
import com.example.ezemkofi.ui.theme.CartItem
import com.example.ezemkofi.ui.theme.CartViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    navController: NavController,
    sharedPreferences: SharedPreferences
){
    val cartItems by cartViewModel.cartItems.collectAsState()

    Surface(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
        color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp) // Reserve space for the button
            ) {
                CartBar(
                    value = stringResource(id = R.string.yourCart),
                    navController = navController,
                    sharedPreferences = sharedPreferences
                )

                LazyColumn(
                    modifier = Modifier.weight(1f) // Ensures LazyColumn takes available space
                ) {
                    items(cartItems) { item ->
                        CoffeeCart(
                            id = item.id,
                            value = item.quantity,
                            size = item.size,
                            name = item.name,
                            type = item.category,
                            unitPrice = item.unitPrice,
                            imagePath = item.imagePath,
                            cartViewModel = cartViewModel // Use the existing cartViewModel
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .align(alignment = Alignment.BottomCenter),
            ){
                // Add the button at the bottom center
                Button(
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            cartViewModel.handleCheckout(cartItems)
                        }
                    },
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier // Center the button horizontally
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.EzemGreen))
                ) {
                    Text(
                        fontSize = 14.sp,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        text = "CHECKOUT",
                        color = Color.White
                    )
                }
            }
        }
    }
}