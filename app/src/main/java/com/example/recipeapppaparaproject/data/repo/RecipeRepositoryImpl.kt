package com.example.recipeapppaparaproject.data.repo

import com.example.recipeapppaparaproject.data.api.RecipeApi
import com.example.recipeapppaparaproject.data.local.RecipeDao
import com.example.recipeapppaparaproject.data.local.entity.FavoriRecipes
import com.example.recipeapppaparaproject.data.model.DailyMealResponse.DailyMealPLanResponse
import com.example.recipeapppaparaproject.data.model.RecipeDetailResponse.RecipeDetailResponse
import com.example.recipeapppaparaproject.data.model.RecipeResponse.RecipeResponse
import com.example.recipeapppaparaproject.utils.ApiResult
import com.example.recipeapppaparaproject.utils.apiFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
class RecipeRepositoryImpl @Inject constructor(
    private val api: RecipeApi,
    private val localDataSource: RecipeDao
    ) : RecipeRepository {
    override suspend fun getRecipes(): Flow<ApiResult<RecipeResponse>> {
        return apiFlow { api.getAllRecipes()}
    }

    override suspend fun getRandomRecipes(): Flow<ApiResult<RecipeResponse>>  {
        return apiFlow { api.getRandomRecipe() }

    }

    //details
    override suspend fun getRecipeDetails(id: Int): Flow<ApiResult<RecipeDetailResponse>> {
        return apiFlow { api.getRecipeDetailsById(id) }
    }

    //category + search
    override suspend fun getRecipesByCategory(query: String): Flow<ApiResult<RecipeResponse>> {
       return apiFlow { api.getRecipesByCategory(query) }
    }
    override suspend fun dailyMealPlan(): Flow<ApiResult<DailyMealPLanResponse>> {
        return apiFlow { api.getMealPlan() }
    }


    //local data recipes

    override suspend fun getFavoriteRecipes(userId: String): Flow<List<FavoriRecipes>> {
       return localDataSource.getFavoriteRecipes(userId)
    }

    override suspend fun insertFavoriteRecipes(favoriRecipes: FavoriRecipes) {
        return localDataSource.insertFavoriteRecipe(favoriRecipes)
    }

    override suspend fun deleteFavoriteRecipeById(recipeId: String) {
        localDataSource.deleteFavoriteRecipeById(recipeId)
    }


}