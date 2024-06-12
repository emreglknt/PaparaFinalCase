package com.example.recipeapppaparaproject.presentation.mealPLanScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapppaparaproject.data.model.DailyMealResponse.DailyMealPLanResponse
import com.example.recipeapppaparaproject.data.model.RecipeResponse.RecipeResponse
import com.example.recipeapppaparaproject.data.repo.RecipeRepository
import com.example.recipeapppaparaproject.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class DailyMealMealState {
    data object Loading : DailyMealMealState()
    data class Success(val recipes: DailyMealPLanResponse) : DailyMealMealState()
    data class Error(val message: String) : DailyMealMealState()
}



@HiltViewModel
class MealPlanViewModel @Inject constructor(private val repository: RecipeRepository): ViewModel()
{
    private val _mealstate = MutableStateFlow<DailyMealMealState>(DailyMealMealState.Loading)
    val mealstate: StateFlow<DailyMealMealState> = _mealstate


    init {
        getMealPlan()
    }

    fun getMealPlan(){
        viewModelScope.launch {
            repository.dailyMealPlan().collect {result->
                when(result){
                    is ApiResult.Success -> {
                        _mealstate.value = DailyMealMealState.Success(result.data!!)
                    }
                    is ApiResult.Error -> {
                        _mealstate.value = DailyMealMealState.Error(result.message!!)
                    }
                    is ApiResult.Loading -> {
                        _mealstate.value = DailyMealMealState.Loading
                    }
                }

            }
        }
    }



}