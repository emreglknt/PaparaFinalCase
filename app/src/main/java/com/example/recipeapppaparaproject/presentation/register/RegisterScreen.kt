package com.example.recipeapppaparaproject.presentation.register

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

import com.airbnb.lottie.compose.*

import com.example.recipeapppaparaproject.R
import com.example.recipeapppaparaproject.presentation.AuthViewModel.AuthViewModel
import java.util.regex.Pattern



@Composable
fun GradientButton(
    text: String,
    gradient: Brush,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    isEnabled: Boolean = true
) {
    val backgroundColor = if (isEnabled) gradient else Brush.horizontalGradient(
        listOf(
            Color.Gray,
            Color.LightGray
        )
    )

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        modifier = modifier
            .background(brush = backgroundColor, shape = RoundedCornerShape(22.dp)),
        enabled = isEnabled
    ) {
        Text(
            text = text,
            color = Color.White,
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun RegisterScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var confirmPassword by remember { mutableStateOf(TextFieldValue("")) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var passwordMatchError by remember { mutableStateOf(false) }
    val gradient = Brush.horizontalGradient(listOf(Color(0xFFFF8C61), Color(0xFF5C374C)))
    val registerResult by viewModel.registerResult.collectAsState(initial = null)
    val context = LocalContext.current


    val emailPattern = Pattern.compile(
        "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    )

    fun validateEmail(email: String): Boolean {
        return emailPattern.matcher(email).matches()
    }

    fun validatePassword(password: String): Boolean {
        return password.length >= 8 && password.any { it.isDigit() } && password.any { it.isUpperCase() }
    }

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.anim)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.foodpattern),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = 0.4f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
                ,
                modifier = Modifier
                    .width(250.dp)
                    .height(250.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Text(
                text = "Create an account",
                style = MaterialTheme.typography.h4.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                ),
                color = Color(0xFF985277),
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .align(Alignment.Start)
            )

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = !validateEmail(email.text)
                },
                label = { Text("Email") },
                isError = emailError,
                leadingIcon = {
                    Icon(
                        Icons.Filled.Email,
                        contentDescription = null,
                        tint = Color(0xFF985277),
                        modifier = Modifier.size(24.dp)
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFFFF8C61),
                    unfocusedBorderColor = Color(0xFF985277),
                    cursorColor = Color(0xFFFF8C61)

                ),
                modifier = Modifier.fillMaxWidth()
            )
            if (emailError) {
                Text(
                    text = "Please enter a valid email address",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = !validatePassword(password.text)
                },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                isError = passwordError,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.lock),
                        contentDescription = null,
                        tint = Color(0xFF985277),
                        modifier = Modifier.size(24.dp)
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFFFF8C61),
                    unfocusedBorderColor = Color(0xFF985277),
                    cursorColor = Color(0xFFFF8C61)
                ),
                modifier = Modifier.fillMaxWidth()
            )
            if (passwordError) {
                Text(
                    text = "Password must contain at least 8 characters, 1 uppercase letter and 1 number",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    passwordMatchError = confirmPassword.text != password.text
                },
                label = { Text("Verify password") },
                visualTransformation = PasswordVisualTransformation(),
                isError = passwordMatchError,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.lock),
                        contentDescription = null,
                        tint = Color(0xFF985277),
                        modifier = Modifier.size(24.dp)
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFFFF8C61),
                    unfocusedBorderColor = Color(0xFF985277),
                    cursorColor = Color(0xFFFF8C61)
                ),
                modifier = Modifier.fillMaxWidth()
            )
            if (passwordMatchError) {
                Text(
                    text = "Passwords do not match",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            GradientButton(
                text = "Sign Up",
                gradient = gradient,
                onClick = {
                    emailError = !validateEmail(email.text)
                    passwordError = !validatePassword(password.text)
                    passwordMatchError = password.text != confirmPassword.text


                    if (!emailError && !passwordError && !passwordMatchError) {
                        viewModel.signup(email.text, password.text)
                    }
                }
            )
            LaunchedEffect(registerResult) {
                registerResult?.let { user ->
                    if (user != null) {
                        navController.navigate("login_screen")
                    } else {
                        Toast.makeText(context, "Registration Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text(
                    text = "If you already have an account, go to ",
                    style = MaterialTheme.typography.body2,
                    color = Color.Gray
                )
                Text(
                    text = "Login!",
                    style = MaterialTheme.typography.body2,
                    color = Color(0xFFFF8C61),
                    modifier = Modifier.clickable {
                        // login sayfasina yonlendirme
                        navController.navigate("login_screen")
                    }
                )
            }
        }
    }
}