package com.example.ezemkofi.ui.theme

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ezemkofi.dataclass.CoffeeDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class CoffeeListViewModel: ViewModel() {
    private val _coffeeList = MutableLiveData<List<Coffee>>()
    val coffeeList: LiveData<List<Coffee>> get() = _coffeeList

    private val _filteredCoffeeList = MutableLiveData<List<Coffee>>()
    val filteredCoffeeList: LiveData<List<Coffee>> get() = _filteredCoffeeList

    private val _topPicks = MutableLiveData<List<Coffee>>()
    val topPicks: LiveData<List<Coffee>> get() = _topPicks

    private val _filteredCoffeeListByName = MutableLiveData<List<Coffee>>(emptyList())
    val filteredCoffeeListByName: LiveData<List<Coffee>> get() = _filteredCoffeeListByName

    private val _coffeeDetails = MutableLiveData<CoffeeDetails?>()
    val coffeeDetails: LiveData<CoffeeDetails?> get() = _coffeeDetails

    fun searchCoffee(query: String) {
        val allCoffees = _coffeeList.value ?: emptyList()
        val filteredList = if (query.isEmpty()) {
            allCoffees
        } else {
            allCoffees.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }
        _filteredCoffeeListByName.value = filteredList
    }

    fun fetchCoffeeList() {
        viewModelScope.launch {
            val coffeeList = withContext(Dispatchers.IO) {
                fetchCoffeeListFromApi()
            }
            _coffeeList.value = coffeeList
            _filteredCoffeeList.value = coffeeList
        }
    }

    fun fetchTopPicks() {
        viewModelScope.launch {
            val topPicks = withContext(Dispatchers.IO) {
                fetchTopPicksFromApi()
            }
            _topPicks.value = topPicks
        }
    }

    fun filterCoffeeByCategory(category: CoffeeCategory?) {
        if (category == null) {
            _filteredCoffeeList.value = _coffeeList.value
        } else {
            _filteredCoffeeList.value = _coffeeList.value?.filter { it.category == category.name }
        }
    }

    private fun fetchCoffeeListFromApi(): List<Coffee> {
        val url = URL("http://192.168.1.6:5000/api/coffee")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        return try {
            connection.inputStream.bufferedReader().use {
                val response = it.readText()
                val jsonArray = JSONArray(response)
                val coffeeList = mutableListOf<Coffee>()
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val coffee = Coffee(
                        id = jsonObject.getString("id"),
                        name = jsonObject.getString("name"),
                        category = jsonObject.getString("category"),
                        rating = jsonObject.getString("rating"), // Ensure the key matches your JSON response
                        price = jsonObject.getString("price"),
                        imagePath = jsonObject.getString("imagePath")
                    )
                    coffeeList.add(coffee)
                }
                coffeeList
            }
        } catch (e: Exception) {
            Log.e("CoffeeListViewModel", "Error fetching coffee list", e)
            emptyList()
        } finally {
            connection.disconnect()
        }
    }

    private fun fetchTopPicksFromApi(): List<Coffee> {
        val url = URL("http://192.168.1.6:5000/api/coffee/top-picks")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        return try {
            connection.inputStream.bufferedReader().use {
                val response = it.readText()
                val jsonArray = JSONArray(response)
                val topPicks = mutableListOf<Coffee>()
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val coffee = Coffee(
                        id = jsonObject.getString("id"),
                        name = jsonObject.getString("name"),
                        category = jsonObject.getString("category"),
                        rating = jsonObject.getString("rating"),
                        price = jsonObject.getString("price"),
                        imagePath = jsonObject.getString("imagePath")
                    )
                    topPicks.add(coffee)
                }
                topPicks
            }
        } finally {
            connection.disconnect()
        }
    }

    fun fetchImage(url: String, callback: (Bitmap?) -> Unit) {
        viewModelScope.launch {
            val bitmap = withContext(Dispatchers.IO) {
                downloadImage(url)
            }
            callback(bitmap)
        }
    }

    fun fetchCoffeeDetails(coffeeId: String) {
        viewModelScope.launch {
            val coffeeDetails = withContext(Dispatchers.IO) {
                fetchCoffeeDetailsFromApi(coffeeId)
            }
            _coffeeDetails.value = coffeeDetails
        }
    }

    private fun fetchCoffeeDetailsFromApi(coffeeId: String): CoffeeDetails? {
        val url = URL("http://192.168.1.6:5000/api/coffee/$coffeeId")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        return try {
            connection.inputStream.bufferedReader().use {
                val response = it.readText()
                val jsonObject = JSONObject(response)
                CoffeeDetails(
                    id = jsonObject.getString("id"),
                    name = jsonObject.getString("name"),
                    description = jsonObject.getString("description"),
                    category = jsonObject.getString("category"),
                    rating = jsonObject.getString("rating"),
                    price = jsonObject.getString("price"),
                    imagePath = jsonObject.getString("imagePath")
                )
            }
        } catch (e: Exception) {
            Log.e("CoffeeListViewModel", "Error fetching coffee details", e)
            null
        } finally {
            connection.disconnect()
        }
    }
}