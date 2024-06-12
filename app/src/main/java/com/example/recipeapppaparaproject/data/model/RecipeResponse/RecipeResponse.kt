package com.example.recipeapppaparaproject.data.model.RecipeResponse


data class RecipeResponse(
    val number: Int,
    val offset: Int,
    val results: List<Result>,
    val totalResults: Int
)