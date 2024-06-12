package com.example.recipeapppaparaproject.presentation.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapppaparaproject.data.local.entity.FavoriRecipes
import com.example.recipeapppaparaproject.data.model.RecipeResponse.RecipeResponse
import com.example.recipeapppaparaproject.data.repo.RecipeRepository
import com.example.recipeapppaparaproject.utils.ApiResult
import com.example.recipeapppaparaproject.utils.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.example.recipeapppaparaproject.data.model.RecipeResponse.Result


sealed class RecipeState {
    object Loading : RecipeState()
    data class Success(val recipes: RecipeResponse) : RecipeState()
    data class Error(val message: String) : RecipeState()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RecipeRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _recipeState = MutableStateFlow<RecipeState>(RecipeState.Loading)
    val recipeState: StateFlow<RecipeState> = _recipeState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        getRecipes()
    }

    private fun getRecipes() {
        viewModelScope.launch {
            _isRefreshing.value = true
            if (NetworkUtils.isOnline(getApplication())) {
                repository.getRecipes().collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            _recipeState.value = RecipeState.Success(result.data!!)
                        }
                        is ApiResult.Error -> {
                            Log.e("HomeViewModel", "Error fetching recipes: ${result.message}")
                            _recipeState.value = RecipeState.Error("Error fetching recipes: ${result.message}")
                        }
                        is ApiResult.Loading -> {
                            _recipeState.value = RecipeState.Loading
                        }
                    }
                }
            } else {
                // Fetch local favorite recipes if offline
                repository.getFavoriteRecipes().collect { result ->
                    val mappedResults = result.map { it.toRecipeResult() }
                    val localRecipes = RecipeResponse(
                        number = mappedResults.size,
                        offset = 0,
                        results = mappedResults,
                        totalResults = mappedResults.size
                    )
                    _recipeState.value = RecipeState.Success(localRecipes)
                }
            }
            _isRefreshing.value = false
        }
    }

    fun getRecipesByCategory(query: String) {
        viewModelScope.launch {
            _isRefreshing.value = true
            repository.getRecipesByCategory(query).collect {
                when (it) {
                    is ApiResult.Success -> {
                        _recipeState.value = RecipeState.Success(it.data!!)
                    }
                    is ApiResult.Error -> {
                        Log.e("HomeViewModel", "Error fetching recipes: ${it.message}")
                        _recipeState.value = RecipeState.Error("Error fetching recipes: ${it.message}")
                    }
                    is ApiResult.Loading -> {
                        _recipeState.value = RecipeState.Loading
                    }
                }
            }
            _isRefreshing.value = false
        }
    }


    fun refreshRecipes() {
        getRecipes()
    }

}


private fun FavoriRecipes.toRecipeResult(): Result {
    return Result(
        id = this.recipeid,
        title = this.title,
        image = this.image,
        imageType = this.imageType
    )
}
