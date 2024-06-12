package com.example.recipeapppaparaproject.presentation.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.recipeapppaparaproject.R
import com.example.recipeapppaparaproject.presentation.AuthViewModel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isFormValid = email.isNotEmpty() && password.length >= 6
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val loginResult = viewModel.loginResult.collectAsState(initial = null)

    Image(
        painter = painterResource(id = R.drawable.foodpattern),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        color = Color.White.copy(alpha = 0.3f)
    ) {
        Column {

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


            Spacer(modifier = Modifier.height(5.dp))
            HeadingTextComponent(heading = "Login")
            Spacer(modifier = Modifier.height(10.dp))
            Column {
                MyTextField(labelVal = "E-mail", Icons.Filled.Email, textVal = email) { email = it }
                Spacer(modifier = Modifier.height(15.dp))
                PasswordTextComponent(labelVal = "Password", password = password) { password = it }
                Spacer(modifier = Modifier.height(20.dp))

                Column {
                    GradientButton(
                        text = "Login",
                        onClick = {
                            scope.launch {
                                //firebase kullanıcı girişi
                                viewModel.login(email, password)
                                if (loginResult != null) {
                                    navController.navigate("main_screen")
                                } else {
                                    Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        },
                        isEnabled = isFormValid
                    )
                    Spacer(modifier = Modifier.height(60.dp))
                    BottomLoginTextComponent(
                        initialText = "Haven't we seen you around here before? ",
                        action = "Join us!",
                        navController = navController
                    )
                }
            }
        }
    }
}



@Composable
fun GradientButton(text: String, onClick: () -> Unit, isEnabled: Boolean) {
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(Color(0xFFFF8C61), Color(0xFF5C374C)),
        startX = 0f,
        endX = 1000f
    )
    val backgroundColor = if (isEnabled) gradientBrush else Brush.horizontalGradient(
        listOf(
            Color.Gray,
            Color.LightGray
        )
    )

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .background(brush = backgroundColor, shape = RoundedCornerShape(22.dp)),
        enabled = isEnabled
    ) {
        Text(
            text = text,
            color = Color.White,
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
        )
    }
}


@Composable
fun HeadingTextComponent(heading: String) {
    Text(
        text = heading,
        modifier = Modifier.fillMaxWidth(),
        fontSize = 39.sp,
        color = Color(0xFF985277),
        fontWeight = FontWeight.Bold
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextField(
    labelVal: String,
    icon: ImageVector,
    textVal: String,
    onValueChange: (String) -> Unit
) {
    val typeOfKeyboard: KeyboardType = when (labelVal) {
        "email ID" -> KeyboardType.Email
        "mobile" -> KeyboardType.Phone
        else -> KeyboardType.Text
    }

    OutlinedTextField(
        value = textVal,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFFFF8C61),
            unfocusedBorderColor = Color(0xFF985277),
            focusedLeadingIconColor = Color(0xFFFF8C61),
            unfocusedLeadingIconColor = Color(0xFF985277)
        ),
        shape = MaterialTheme.shapes.small,
        placeholder = {
            Text(text = labelVal, color = Color(0xFF985277))
        },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = "at_symbol"
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = typeOfKeyboard,
            imeAction = ImeAction.Done
        ),
        singleLine = true
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextComponent(labelVal: String, password: String, onValueChange: (String) -> Unit) {
    var isShowPassword by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFFFF8C61),
            unfocusedBorderColor = Color(0xFF985277),
        ),
        shape = MaterialTheme.shapes.small,
        placeholder = {
            Text(text = labelVal, color = Color(0xFF985277))
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = "password_lock",
                tint = Color(0xFF985277)
            )
        },
        trailingIcon = {
            val description = if (isShowPassword) "Show Password" else "Hide Password"
            val iconImage = if (isShowPassword) Icons.Filled.Lock else Icons.Filled.Close

            IconButton(onClick = {
                isShowPassword = !isShowPassword
            }) {
                Icon(
                    imageVector = iconImage,
                    contentDescription = description,
                    tint = Color(0xFF985277),
                )
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (isShowPassword) VisualTransformation.None else PasswordVisualTransformation(),
        singleLine = true
    )
}


@Composable
fun BottomLoginTextComponent(initialText: String, action: String, navController: NavController) {
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color(0xFF985277))) {
            append(initialText)
        }
        withStyle(style = SpanStyle(color = Color(0xFFFF8C61), fontWeight = FontWeight.Bold)) {
            pushStringAnnotation(tag = action, annotation = action)
            append(action)
        }
    }
    ClickableText(text = annotatedString, onClick = {
        annotatedString.getStringAnnotations(it, it)
            .firstOrNull()?.let { span ->
                if (span.item == "Join us!") {
                    //Register sayfasına yönlendir
                    navController.navigate("register_screen")
                }
            }
    })
}