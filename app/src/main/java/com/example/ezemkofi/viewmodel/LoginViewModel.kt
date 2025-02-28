package com.example.ezemkofi.ui.theme

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.log

class LoginViewModel(private val context: Context) : ViewModel() {
    private val tokenManager = TokenManager(context)
    private val _loginResult = MutableLiveData<Result<User>>()
    val loginResult: LiveData<Result<User>> get() = _loginResult

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    init {
        // Check if the user is already logged in
        if (tokenManager.isLoggedIn()) {
            viewModelScope.launch {
                try {
                    val token = tokenManager.getToken()
                    if (token != null) {
                        val user = fetchUserInfo()
                        _user.postValue(user)
                    }
                } catch (e: Exception) {
                    Log.e("LoginViewModel", "Error fetching user info", e)
                }
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val token = performLogin(username, password)
                if (token != null) {
                    Log.d("LoginViewModel", "Received token: $token")
                    tokenManager.saveToken(token, true) // Save token to TokenManager
                    val user = fetchUserInfo()
                    Log.d("CekUser", "$user")
                    Log.d("TokenCek", "$token")
                    if (user != null) {
                        _loginResult.postValue(Result.success(user))
                    } else {
                        _loginResult.postValue(Result.failure(Exception("Failed to fetch user info")))
                    }
                } else {
                    _loginResult.postValue(Result.failure(Exception("Failed to login")))
                }
            } catch (e: Exception) {
                _loginResult.postValue(Result.failure(e))
            }
        }
    }

    fun logout(){
        tokenManager.clearToken()
        _user.postValue(null)
    }

    private suspend fun performLogin(username: String, password: String): String? {
        return withContext(Dispatchers.IO) {
            val url = URL("http://192.168.1.6:5000/api/auth")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")
            connection.doOutput = true

            val jsonInputString = JSONObject().apply {
                put("username", username)
                put("password", password)
            }.toString()

            Log.d("CheckApi", jsonInputString)

            try {
                OutputStreamWriter(connection.outputStream).use { os ->
                    os.write(jsonInputString)
                    os.flush()
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val responseStream = connection.inputStream.bufferedReader().use { it.readText() }
                    Log.d("NetworkUtils", "POST Response: $responseStream")
                    responseStream // Sesuaikan dengan struktur JSON respons
                } else {
                    Log.e("Network", "Http POST request failed with response code $responseCode")
                    val errorStream = connection.errorStream.bufferedReader().use { it.readText() }
                    Log.e("Network", "Error Response: $errorStream")
                    null
                }
            } finally {
                connection.disconnect()
            }
        }
    }

    private suspend fun fetchUserInfo(): User? {
        return withContext(Dispatchers.IO) {
            val token = tokenManager.getToken()
            Log.d("CekToken", "$token")
            val url = URL("http://192.168.1.6:5000/api/me")
            val tokenNew = token.toString()
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            val cleanToken = tokenNew.replace(Regex("[\"']"), "")
            Log.d("TokenNew", "$cleanToken")

            connection.setRequestProperty("Authorization", "Bearer $cleanToken")
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")

            Log.d("NetworkUtils", "GET request to ${url} with token: $cleanToken")

            try {
                val responseCode = connection.responseCode
                Log.d("CekResponse", "$responseCode")
                return@withContext if (responseCode == HttpURLConnection.HTTP_OK) {
                    val responseStream = connection.inputStream.bufferedReader().use { it.readText() }
                    Log.d("NetworkUtils", "GET Response: $responseStream")
                    val jsonResponse = JSONObject(responseStream)
                    tokenManager.saveFullName(jsonResponse.optString("fullName"))
                    User(jsonResponse.optString("fullName")) // Sesuaikan dengan struktur JSON
                } else {
                    val errorResponse = connection.errorStream?.bufferedReader()?.use { it.readText() }
                    Log.e("Network", "Http GET request failed with response code $responseCode")
                    Log.e("Network", "Error Response: $errorResponse")
                    null
                }
            } finally {
                connection.disconnect()
            }
        }
    }
}
