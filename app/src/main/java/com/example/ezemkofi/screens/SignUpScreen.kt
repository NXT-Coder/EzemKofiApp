package com.example.ezemkofi.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ezemkofi.R
import com.example.ezemkofi.components.ClickAbleLoginText
import com.example.ezemkofi.components.EmailTextField
import com.example.ezemkofi.components.FieldText
import com.example.ezemkofi.components.HeaderText
import com.example.ezemkofi.components.ImageLogo
import com.example.ezemkofi.components.LoginOrSignButton
import com.example.ezemkofi.components.PasswordTextField
import com.example.ezemkofi.components.Textfield
import com.example.ezemkofi.ui.theme.SignUpViewModel
import com.example.ezemkofi.ui.theme.SignUpViewModelFactory

@Composable
fun SignUpScreen(navController: NavHostController){
    val context = LocalContext.current
    val viewModel: SignUpViewModel = viewModel(factory = SignUpViewModelFactory(context))
    var username by rememberSaveable { mutableStateOf("") }
    var fullname by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    val signUpResult by viewModel.signUpResult.observeAsState()
    val user by viewModel.user.observeAsState()

    LaunchedEffect(user) {
        user?.let {
            navController.navigate("main/${it.fullName}") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    LaunchedEffect(signUpResult) {
        signUpResult?.let {
            if (it.isSuccess) {
                val user = it.getOrNull()
                Toast.makeText(context, "Sign up successful!", Toast.LENGTH_LONG).show()
                // Navigate to MainScreen with user data
                navController.navigate("main/${user?.fullName}") {
                    popUpTo("signUp") { inclusive = true }
                }
            } else {
                Toast.makeText(context, "Username is exist", Toast.LENGTH_LONG).show()
            }
        }
    }

    Surface(
        modifier = Modifier
            .heightIn(35.dp)
            .background(Color.White),
        color = Color.White
    ) {
        val scrollState = rememberScrollState()

        Box(
            modifier = Modifier.fillMaxSize()
        ){
            Column(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)) {
                ImageLogo()
                Spacer(modifier = Modifier.heightIn(20.dp))
                HeaderText(
                    text = stringResource(id = R.string.create),
                    value = stringResource(id = R.string.createText)
                )
                Spacer(modifier = Modifier.heightIn(20.dp))
                FieldText(text = stringResource(id = R.string.username))
                Spacer(modifier = Modifier.heightIn(5.dp))
                Textfield(
                    text = stringResource(id = R.string.username),
                    value = username,
                    onUsernameChange = {
                        username = it
                    },
                )
                FieldText(text = stringResource(id = R.string.fullname))
                Spacer(modifier = Modifier.heightIn(5.dp))
                Textfield(
                    text = stringResource(id = R.string.fullname),
                    value = fullname,
                    onUsernameChange = {
                        fullname = it
                    }
                )
                FieldText(text = stringResource(id = R.string.email))
                Spacer(modifier = Modifier.heightIn(5.dp))
                EmailTextField(
                    email = email,
                    onEmailChange = {
                        email = it
                    },
                )
                FieldText(text = stringResource(id = R.string.password))
                Spacer(modifier = Modifier.heightIn(5.dp))
                PasswordTextField(
                    password = password,
                    onPasswordChange = {
                        password = it
                    }
                )
                FieldText(text = stringResource(id = R.string.confirmPassword))
                Spacer(modifier = Modifier.heightIn(5.dp))
                PasswordTextField(
                    password = confirmPassword,
                    onPasswordChange = {
                        confirmPassword = it
                    }
                )
                LoginOrSignButton(
                    value = stringResource(id = R.string.signUp),
                    onClick = {
                        if(username.isEmpty() || fullname.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
                            Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_LONG).show()
                        } else if(password != confirmPassword){
                            Toast.makeText(context, "Password don't match", Toast.LENGTH_LONG).show()
                        } else if(password.length < 4){
                            Toast.makeText(context, "Password must be at least 4 characters long", Toast.LENGTH_LONG).show()
                        } else {
                            viewModel.signUp(username, fullname, email, password)
                        }
                    }
                )
                ClickAbleLoginText(
                    value1 = stringResource(id = R.string.alreadyHave),
                    value2 = stringResource(id = R.string.loginHere),
                    onClick = {
                        navController.navigate("login")
                    }
                )
                Spacer(modifier = Modifier.heightIn(20.dp))
            }
        }
    }
}