package com.example.recipeapppaparaproject.data.model.RecipeDetailResponse

import kotlinx.serialization.Serializable

data class RecipeDetailResponse(
    val aggregateLikes: Int,
    val analyzedInstructions: List<Any>,
    val cheap: Boolean,
    val cookingMinutes: Any,
    val creditsText: String,
    val cuisines: List<Any>,
    val dairyFree: Boolean,
    val diets: List<Any>,
    val dishTypes: List<String>,
    val extendedIngredients: List<ExtendedIngredient>,
    val gaps: String,
    val glutenFree: Boolean,
    val healthScore: Int,
    val id: Int,
    val image: String,
    val imageType: String,
    val instructions: String,
    val license: String,
    val lowFodmap: Boolean,
    val occasions: List<Any>,
    val originalId: Any,
    val preparationMinutes: Any,
    val pricePerServing: Double,
    val readyInMinutes: Int,
    val servings: Int,
    val sourceName: String,
    val sourceUrl: String,
    val spoonacularScore: Double,
    val spoonacularSourceUrl: String,
    val summary: String,
    val sustainable: Boolean,
    val title: String,
    val vegan: Boolean,
    val vegetarian: Boolean,
    val veryHealthy: Boolean,
    val veryPopular: Boolean,
    val weightWatcherSmartPoints: Int
)

@Serializable
data class AnalyzedInstruction(
    val name: String = "",
    val steps: List<Step> = emptyList()
)

@Serializable
data class Step(
    val number: Int = 0,
    val step: String = "",
    val ingredients: List<Ingredient> = emptyList(),
    val equipment: List<Equipment> = emptyList()
)

@Serializable
data class Ingredient(
    val id: Int = 0,
    val name: String = "",
    val image: String = ""
)

@Serializable
data class Equipment(
    val id: Int = 0,
    val name: String = "",
    val image: String = ""
)

