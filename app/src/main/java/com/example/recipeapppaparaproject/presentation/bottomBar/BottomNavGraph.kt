package com.example.recipeapppaparaproject.presentation.bottomBar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.recipeapppaparaproject.presentation.home.HomeScreen
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.recipeapppaparaproject.data.local.entity.FavoriRecipes
import com.example.recipeapppaparaproject.presentation.favoriteRecipes.FavoriteRecipeScreen
import com.example.recipeapppaparaproject.presentation.login.LoginScreen
import com.example.recipeapppaparaproject.presentation.mealPLanScreen.MealPlanScreen
import com.example.recipeapppaparaproject.presentation.recipeDetail.RecipeDetailScreen
import com.example.recipeapppaparaproject.presentation.recipeDetail.RecipeDetailViewModel
import com.example.recipeapppaparaproject.utils.Constants.RECIPE_ID
import com.google.firebase.auth.FirebaseAuth


@Composable
fun BottomNavGraph(
    navController: NavHostController
) {
    val user = FirebaseAuth.getInstance().currentUser
    val userId = user?.uid ?: ""

    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.Home.route) {
            Box(modifier = Modifier.padding(bottom = 56.dp)) {
                HomeScreen(navController = navController)
            }
        }
        composable(route = BottomBarScreen.Favourite.route + "/{userId}") {
            Box(modifier = Modifier.padding(bottom = 56.dp)) {
                FavoriteRecipeScreen(navController = navController, userId = userId)
            }
        }

        composable(
            route = BottomBarScreen.MealDetails.route,
            arguments = listOf(navArgument("mealId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val mealId = backStackEntry.arguments?.getInt("mealId") ?: 0
            // Save the mealId to SavedStateHandle
            val viewModel: RecipeDetailViewModel = hiltViewModel()
            LaunchedEffect(mealId) {
                viewModel.savedStateHandle["recipeId"] = mealId
            }
            Box(modifier = Modifier.padding(bottom = 56.dp)) {
                RecipeDetailScreen(onBackMealsScreen = { navController.popBackStack() })
            }
        }

        composable(route = BottomBarScreen.DailyMealPlan.route) {
            Box(modifier = Modifier.padding(bottom = 56.dp)) {
                MealPlanScreen(navController = navController)
            }
        }

        composable(route = BottomBarScreen.Login.route) {
            Box(modifier = Modifier.padding(bottom = 56.dp)) {
                LoginScreen(navController = navController)
            }
        }
    }
}
