package com.example.recipeapppaparaproject

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build

import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface

import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.recipeapppaparaproject.ui.theme.RecipeAppPaparaProjectTheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.recipeapppaparaproject.presentation.bottomBar.BottomNav
import com.example.recipeapppaparaproject.presentation.home.HomeScreen
import com.example.recipeapppaparaproject.presentation.login.LoginScreen
import com.example.recipeapppaparaproject.presentation.register.RegisterScreen

import com.example.recipeapppaparaproject.ui.theme.RecipeAppPaparaProjectTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeAppPaparaProjectTheme {
                val navController = rememberNavController()
                Surface(modifier = Modifier.fillMaxSize()) {
                    SetupNavGraph(navController = navController)
                }
                scheduleNotification()
            }
        }

    }


    private fun scheduleNotification() {
        val notificationWork: WorkRequest =
            OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(1, TimeUnit.HOURS) // Change delay as needed
                .build()
        WorkManager.getInstance(this).enqueue(notificationWork)
    }


}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    Scaffold(
        bottomBar = { BottomNav() },
    ) {
    }
}




@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login_screen",
    ) {

        composable("login_screen") {
            LoginScreen(navController = navController)
        }
        composable("register_screen") {
            RegisterScreen(navController = navController)
        }
        composable("main_screen") {
            MainScreen()
        }
    }
}
