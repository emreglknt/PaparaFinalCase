package com.example.recipeapppaparaproject.presentation.recipeDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapppaparaproject.data.model.RecipeDetailResponse.RecipeDetailResponse
import com.example.recipeapppaparaproject.data.repo.RecipeRepository
import com.example.recipeapppaparaproject.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class RecipeDetailState {
    data object Loading : RecipeDetailState()
    data class Success(val meals: RecipeDetailResponse) : RecipeDetailState()
    data class Error(val message: String) : RecipeDetailState()
}


@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val repo: RecipeRepository,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {


    // uı ve lifecycle durumuna göre view model state ini değiştirmek/korumak için
    private val _selectRecipe = MutableStateFlow<RecipeDetailState>(RecipeDetailState.Loading)
    val selectRecipe: StateFlow<RecipeDetailState> = _selectRecipe





     fun getRecipeDetail(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {

                repo.getRecipeDetails(id).collect { apiResult ->
                    when (apiResult) {
                        is ApiResult.Success -> {
                            _selectRecipe.value = RecipeDetailState.Success(apiResult.data!!)
                        }
                        is ApiResult.Error -> {
                            _selectRecipe.value = RecipeDetailState.Error(apiResult.message!!)
                        }
                        ApiResult.Loading -> {
                            _selectRecipe.value = RecipeDetailState.Loading

                     }
                }
            }
        }
    }





}