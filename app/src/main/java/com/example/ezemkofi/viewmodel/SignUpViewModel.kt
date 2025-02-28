package com.example.ezemkofi.ui.theme

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class SignUpViewModel(private val context: Context) : ViewModel() {
    private val tokenManager = TokenManager(context)
    private val _signUpResult = MutableLiveData<Result<User>>()
    val signUpResult: LiveData<Result<User>> get() = _signUpResult

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
                    Log.e("SignUpViewModel", "Error fetching user info", e)
                }
            }
        }
    }

    fun signUp(username: String, fullName: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val token = performSignUp(username, fullName, email, password)
                if (token != null) {
                    tokenManager.saveToken(token, true)
                    val user = fetchUserInfo()
                    if (user != null) {
                        _signUpResult.postValue(Result.success(user))
                    } else {
                        _signUpResult.postValue(Result.failure(Exception("Failed to fetch user info")))
                    }
                } else {
                    _signUpResult.postValue(Result.failure(Exception("Failed to SignUp")))
                }
            } catch (e: Exception) {
                _signUpResult.postValue(Result.failure(e))
            }
        }
    }

    private suspend fun performSignUp(username: String, fullname: String, email: String, password: String): String {
        return withContext(Dispatchers.IO) {
            val url = URL("http://192.168.1.6:5000/api/register")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")
            connection.doOutput = true

            val jsonInputString = JSONObject().apply {
                put("username", username)
                put("fullname", fullname)
                put("email", email)
                put("password", password)
            }.toString()

            Log.d("CheckInput", jsonInputString)

            try {
                OutputStreamWriter(connection.outputStream).use { os ->
                    os.write(jsonInputString)
                    os.flush()
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val responseStream = connection.inputStream.bufferedReader().use { it.readText() }
                    Log.d("NetworkSignUp", "POST Response: $responseStream")
                    return@withContext responseStream
                } else {
                    Log.e("NetworkSignUp", "Http POST request failed with response code $responseCode")
                    val errorStream = connection.errorStream.bufferedReader().use { it.readText() }
                    return@withContext errorStream
                }
            } finally {
                connection.disconnect()
            }
        }
    }

    private suspend fun fetchUserInfo(): User? {
        return withContext(Dispatchers.IO) {
            val token = tokenManager.getToken()
            val url = URL("http://192.168.1.6:5000/api/me")
            val connection = url.openConnection() as HttpURLConnection
            val tokenNew = token.toString()
            val cleanTokenSignUp = tokenNew.replace(Regex("[\"']"), "")
            connection.requestMethod = "GET"
            connection.setRequestProperty("Authorization", "Bearer $cleanTokenSignUp")
            connection.setRequestProperty("Accept", "application/json")

            Log.d("NetworkUtils", "GET request to ${url.toString()} with token: $cleanTokenSignUp")

            try {
                val responseCode = connection.responseCode
                return@withContext if (responseCode == HttpURLConnection.HTTP_OK) {
                    val responseStream = connection.inputStream.bufferedReader().use { it.readText() }
                    Log.d("NetworkUtils", "GET Response: $responseStream")
                    val jsonResponse = JSONObject(responseStream)
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