package com.example.recipeapppaparaproject.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.recipeapppaparaproject.data.local.entity.FavoriRecipes

@Database(entities = [FavoriRecipes::class], version = 2, exportSchema = false)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao

}