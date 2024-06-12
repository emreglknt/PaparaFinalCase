package com.example.recipeapppaparaproject.presentation.bottomBar

import com.example.recipeapppaparaproject.R

sealed class BottomBarScreen(
    val route: String,
    val title: String,
//    val icon: Int,
//    val icon_focused: Int
) {

    data object Home : BottomBarScreen(
        route = "home",
        title = "Home",
//        icon = R.drawable.ic_home,
//        icon_focused = R.drawable.ic_home
    )

    data object MealDetails : BottomBarScreen(
        route = "meal_details/{mealId}",
        title = "Meal Details",
//        icon = R.drawable.ic_home,
//        icon_focused = R.drawable.ic_home
    ) {
        fun passMealId(mealId: Int): String {
            return "meal_details/$mealId"
        }
    }

    data object Favourite : BottomBarScreen(
        route = "favourite/{userId}",
        title = "Favourite",
//        icon = R.drawable.ic_fav,
//        icon_focused = R.drawable.ic_fav
    )

    data object DailyMealPlan : BottomBarScreen(
        route = "daily_meal_plan",
        title = "Meal Plan",
//        icon = R.drawable.ic_meal_plan,
//        icon_focused = R.drawable.ic_meal_plan
    )

    data object Logout : BottomBarScreen(
        route = "logout",
        title = "Logout",
//        icon = R.drawable.ic_logout,
//        icon_focused = R.drawable.ic_logout
    )

    data object Login : BottomBarScreen(
        route = "login",
        title = "Login",
//        icon = R.drawable.ic_logout,
//        icon_focused = R.drawable.ic_logout
    )





}