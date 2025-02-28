package com.example.ezemkofi.ui.theme

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class CoffeeViewModel : ViewModel() {

    private val _categories = MutableLiveData<List<CoffeeCategory>>()
    val categories: LiveData<List<CoffeeCategory>> get() = _categories

    fun fetchCategories() {
        viewModelScope.launch {
            val categories = getCategoriesFromApi()
            _categories.postValue(categories)
        }
    }

    private suspend fun getCategoriesFromApi(): List<CoffeeCategory> {
        return withContext(Dispatchers.IO) {
            val url = URL("http://192.168.1.6:5000/api/coffee-category")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/json")

            return@withContext try {
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val responseStream = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonArray = JSONArray(responseStream)
                    val categories = mutableListOf<CoffeeCategory>()

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val category = CoffeeCategory(
                            id = jsonObject.getInt("id"),
                            name = jsonObject.getString("name")
                        )
                        categories.add(category)
                    }
                    categories
                } else {
                    Log.e("API Error", "Response Code: $responseCode")
                    emptyList()
                }
            } finally {
                connection.disconnect()
            }
        }
    }
}