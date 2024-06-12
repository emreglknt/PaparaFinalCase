package com.example.recipeapppaparaproject.data.model.RecipeResponse

import android.os.Parcelable
import kotlinx.serialization.Serializable


@Serializable
data class Result(
    val id: Int,
    val image: String,
    val imageType: String,
    val title: String
)