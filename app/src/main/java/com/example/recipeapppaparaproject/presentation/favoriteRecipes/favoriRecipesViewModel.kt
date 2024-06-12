package com.example.recipeapppaparaproject.presentation.favoriteRecipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapppaparaproject.data.local.entity.FavoriRecipes
import com.example.recipeapppaparaproject.data.model.RecipeDetailResponse.RecipeDetailResponse
import com.example.recipeapppaparaproject.data.repo.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteRecipesViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _favoriteRecipes = MutableLiveData<List<FavoriRecipes>>()
    val favoriteRecipes: LiveData<List<FavoriRecipes>> = _favoriteRecipes


    fun getFavoriteRecipes(userId: String) {
        viewModelScope.launch {
            repository.getFavoriteRecipes(userId).collect { recipes ->
                _favoriteRecipes.value = recipes
            }
        }
    }

    fun addFavoriteRecipe(recipe: RecipeDetailResponse, userId: String) {
        viewModelScope.launch {
            val favoriRecipe = FavoriRecipes(
                recipeid = recipe.id,
                image = recipe.image,
                imageType = recipe.imageType,
                title = recipe.title,
                userId = userId
            )
            repository.insertFavoriteRecipes(favoriRecipe)
            getFavoriteRecipes(userId) // Update favorite recipes after adding

        }
    }

    fun removeFavoriteRecipe(recipeId: String,userId: String) {
        viewModelScope.launch {
            repository.deleteFavoriteRecipeById(recipeId)
            getFavoriteRecipes(userId) // Update favorite recipes after adding
        }
    }



}