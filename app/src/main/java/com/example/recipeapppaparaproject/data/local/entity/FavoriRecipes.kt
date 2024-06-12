package com.example.recipeapppaparaproject.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favori_recipes")
data class FavoriRecipes(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var recipeid: Int,
    var image: String,
    var imageType: String,
    var title: String,
)
