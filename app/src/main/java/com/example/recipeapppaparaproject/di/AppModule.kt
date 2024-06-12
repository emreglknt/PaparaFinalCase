package com.example.recipeapppaparaproject.di

import android.content.Context
import androidx.room.Room
import com.example.recipeapppaparaproject.data.api.RecipeApi
import com.example.recipeapppaparaproject.data.local.RecipeDao
import com.example.recipeapppaparaproject.data.local.RecipeDatabase
import com.example.recipeapppaparaproject.data.repo.AuthRepository
import com.example.recipeapppaparaproject.data.repo.RecipeRepository
import com.example.recipeapppaparaproject.data.repo.RecipeRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository {
        return AuthRepository(auth)
    }

    //Room database Module
    @Provides
    @Singleton
    fun provideRecipeDao(database: RecipeDatabase): RecipeDao {
        return database.recipeDao()
    }
    @Provides
    @Singleton
    fun provideRecipeDatabase(@ApplicationContext context: Context):RecipeDatabase{
        return Room.databaseBuilder(
            context.applicationContext,
            RecipeDatabase::class.java,
            "recipe_database.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRecipeRepository(api: RecipeApi, localDataSource: RecipeDao): RecipeRepository {
        return RecipeRepositoryImpl(api = api, localDataSource = localDataSource)
    }










}