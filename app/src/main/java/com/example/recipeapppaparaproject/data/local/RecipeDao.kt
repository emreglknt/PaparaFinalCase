package com.example.recipeapppaparaproject.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipeapppaparaproject.data.local.entity.FavoriRecipes
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteRecipe(favoriteRecipe: FavoriRecipes)

    @Query("SELECT * FROM favori_recipes")
    fun getFavoriteRecipes(): Flow<List<FavoriRecipes>>

    @Query("DELETE FROM favori_recipes WHERE recipeid = :recipeId")
    suspend fun deleteFavoriteRecipeById(recipeId: String)


}