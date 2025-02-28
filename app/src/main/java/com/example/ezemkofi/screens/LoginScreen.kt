package com.example.ezemkofi.screens

import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ezemkofi.R
import com.example.ezemkofi.components.ClickAbleLoginText
import com.example.ezemkofi.components.FieldText
import com.example.ezemkofi.components.HeaderText
import com.example.ezemkofi.components.ImageLogo
import com.example.ezemkofi.components.LoginOrSignButton
import com.example.ezemkofi.components.PasswordTextField
import com.example.ezemkofi.components.Textfield
import com.example.ezemkofi.ui.theme.LoginViewModel
import com.example.ezemkofi.ui.theme.LoginViewModelFactory

@Composable
fun LoginScreen(navController: NavHostController, sharedPreferences: SharedPreferences){
    val context = LocalContext.current
    val viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(context))
    val loginResult by viewModel.loginResult.observeAsState()
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val user by viewModel.user.observeAsState()

    LaunchedEffect(user) {
        user?.let {
            navController.navigate("main/${it.fullName}") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    LaunchedEffect(loginResult) {
        Log.d("CheckResult", "$isLoading")
        loginResult?.let {
            Log.d("CheckResult", "$loginResult")
            if (it.isSuccess) {
                val user = it.getOrNull()
                Toast.makeText(context, "Login successful!", Toast.LENGTH_LONG).show()
                // Navigate to MainScreen
                navController.navigate("main/${user?.fullName}") {
                    popUpTo("login") { inclusive = true }
                }
            } else {
                Toast.makeText(context, "Username or password is incorrect", Toast.LENGTH_LONG).show()
            }
        }
    }

    Surface(
        modifier = Modifier
            .heightIn(10.dp)
            .background(Color.White),
        color = Color.White,
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            Column(modifier = Modifier.fillMaxSize()) {
                ImageLogo()
                Spacer(modifier = Modifier.heightIn(120.dp))
                HeaderText(
                    text = stringResource(id = R.string.login),
                    value = stringResource(id = R.string.loginAccount)
                )
                FieldText(text = stringResource(id = R.string.username))
                Spacer(modifier = Modifier.heightIn(5.dp))
                Textfield(
                    text = stringResource(id = R.string.username),
                    value = username,
                    onUsernameChange = {
                        username = it
                        Log.d("CekUsername", "$username")
                    },
                )
                FieldText(text = stringResource(id = R.string.password))
                Spacer(modifier = Modifier.heightIn(5.dp))
                PasswordTextField(
                    password = password,
                    onPasswordChange = {
                        password = it
                        Log.d("CekPass", "$password")
                    },
                )
                LoginOrSignButton(
                    value = stringResource(id = R.string.loginButton),
                    onClick = {
                        if (username.isEmpty() || password.isEmpty()){
                            Toast.makeText(context, "Please fill in both fields", Toast.LENGTH_SHORT).show()
                        }else{
                            viewModel.login(username, password)
                        }
                    },
                )
                ClickAbleLoginText(
                    value1 = stringResource(id = R.string.loginText),
                    value2 = stringResource(id = R.string.createAccount),
                    onClick = {
                        navController.navigate("signUp")
                    }
                )
            }
        }
    }
}