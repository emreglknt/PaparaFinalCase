package com.example.recipeapppaparaproject.data.repo


import com.example.recipeapppaparaproject.data.local.entity.FavoriRecipes
import com.example.recipeapppaparaproject.data.model.DailyMealResponse.DailyMealPLanResponse
import com.example.recipeapppaparaproject.data.model.RecipeDetailResponse.RecipeDetailResponse
import com.example.recipeapppaparaproject.data.model.RecipeResponse.RecipeResponse
import com.example.recipeapppaparaproject.utils.ApiResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RecipeRepository {
    suspend fun getRecipes(): Flow<ApiResult<RecipeResponse>>
    suspend fun getRandomRecipes(): Flow<ApiResult<RecipeResponse>>
    suspend fun getRecipeDetails(id: Int): Flow<ApiResult<RecipeDetailResponse>>
    suspend fun getRecipesByCategory(query: String): Flow<ApiResult<RecipeResponse>>
    suspend fun dailyMealPlan(): Flow<ApiResult<DailyMealPLanResponse>>
    // Local fav recipes
    suspend fun getFavoriteRecipes(userId: String): Flow<List<FavoriRecipes>>
    suspend fun insertFavoriteRecipes(favoriRecipes: FavoriRecipes)
    suspend fun deleteFavoriteRecipeById(recipeId: String)



}

