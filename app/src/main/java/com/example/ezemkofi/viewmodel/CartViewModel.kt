package com.example.ezemkofi.ui.theme


import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class CartViewModel(private val context: Context): ViewModel() {
    private val tokenManager = TokenManager(context)
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    fun addOrUpdateItem(quantity: Int, item: CartItem) {
        val updatedItems = _cartItems.value.toMutableList()
        val existingItemIndex = updatedItems.indexOfFirst {
            it.name == item.name && it.size == item.size
        }

        if (existingItemIndex != -1) {
            val existingItem = updatedItems[existingItemIndex]
            val updatedQuantity = existingItem.quantity + quantity
            val updatedItem = existingItem.copy(quantity = updatedQuantity)
            updatedItems[existingItemIndex] = updatedItem
            Log.d("CartViewModel", "Updated item: $updatedItem")
        } else {
            updatedItems.add(item)
            Log.d("CartViewModel", "Added new item: $item")
        }

        _cartItems.value = updatedItems
    }

    fun addOrUpdateItemQuantity(quantity: Int, item: CartItem) {
        val updatedItems = _cartItems.value.toMutableList()
        val existingItemIndex = updatedItems.indexOfFirst {
            it.name == item.name && it.size == item.size
        }

        if (existingItemIndex != -1) {
            val existingItem = updatedItems[existingItemIndex]
            val updatedQuantity = quantity
            val updatedItem = existingItem.copy(quantity = updatedQuantity)
            updatedItems[existingItemIndex] = updatedItem
            Log.d("CartViewModel", "Updated item: $updatedItem")
        } else {
            updatedItems.add(item)
            Log.d("CartViewModel", "Added new item: $item")
        }

        _cartItems.value = updatedItems
    }

    suspend fun handleCheckout(cartItems: List<CartItem>) {
        val token = tokenManager.getToken()
        val tokenNew = token.toString()
        val cleanToken = tokenNew.replace(Regex("[\"']"), "")
        Log.d("TokenNew", cleanToken)

        val url = URL("http://192.168.1.6:5000/api/checkout")
        val jsonArray = JSONArray()
        for (item in cartItems) {
            val jsonObject = JSONObject().apply {
                put("coffeeId", item.id)
                put("size", item.size)
                put("qty", item.quantity)
            }
            jsonArray.put(jsonObject)
        }
        val jsonPayload = jsonArray.toString()

        withContext(Dispatchers.IO) {
            try {
                (url.openConnection() as? HttpURLConnection)?.run {
                    requestMethod = "POST"
                    setRequestProperty("Authorization", "Bearer $cleanToken")
                    setRequestProperty("Content-Type", "application/json")
                    doOutput = true
                    outputStream.use { os: OutputStream ->
                        os.write(jsonPayload.toByteArray())
                    }

                    Log.d("CekJson", jsonPayload)

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Checkout successful", Toast.LENGTH_SHORT).show()
                            clearCart()
                        }
                        Log.d("ResponseSuccess", "$responseCode")
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Checkout cart is empty!", Toast.LENGTH_SHORT).show()
                        }
                        Log.d("ResponseError", "$responseMessage")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Checkout failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun removeItem(name: String, size: String) {
        val updatedItems = _cartItems.value.toMutableList()
        val itemToRemove = updatedItems.find {
            it.name == name && it.size == size
        }
        itemToRemove?.let {
            updatedItems.remove(it)
        }
        _cartItems.value = updatedItems
    }

    fun fetchImage(url: String, callback: (Bitmap?) -> Unit) {
        viewModelScope.launch {
            val bitmap = withContext(Dispatchers.IO) {
                downloadImage(url)
            }
            callback(bitmap)
        }
    }
}