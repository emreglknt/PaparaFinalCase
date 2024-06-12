package com.example.recipeapppaparaproject.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.recipeapppaparaproject.presentation.favoriteRecipes.FavoriteRecipeScreen
import com.example.recipeapppaparaproject.presentation.home.HomeScreen
import com.example.recipeapppaparaproject.presentation.login.LoginScreen
import com.example.recipeapppaparaproject.presentation.mealPLanScreen.MealPlanScreen
import com.example.recipeapppaparaproject.presentation.recipeDetail.RecipeDetailScreen
import com.example.recipeapppaparaproject.presentation.register.RegisterScreen
import com.example.recipeapppaparaproject.utils.Constants.RECIPE_ID
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavGraph(navController: NavHostController) {

    val currentUser = FirebaseAuth.getInstance().currentUser
    NavHost(navController = navController, startDestination = Screens.LOGIN) {

        composable(Screens.HOME) {
            HomeScreen(
                navController = navController
            )
        }

        composable("login_screen") {
            LoginScreen(navController = navController)
        }

        composable("register_screen") {
            RegisterScreen(navController = navController)
        }
        composable("home_screen") {
            HomeScreen(navController = navController)
        }

        //faviourite
        composable("favourite_screen") {
            FavoriteRecipeScreen(navController = navController,userId = currentUser?.uid ?: "")
        }

        composable("daily_meal_screen"){
            MealPlanScreen(navController = navController)
        }

//        composable(
//            "${Screens.RECIPE_DETAIL}/{$RECIPE_ID}",
//            arguments = listOf(navArgument(RECIPE_ID) {
//                type = NavType.IntType
//            })
//        ) {
//            RecipeDetailScreen(
//                onBackMealsScreen = {
//                    navController.popBackStack()
//                }
//            )
//
//        }


    }
}