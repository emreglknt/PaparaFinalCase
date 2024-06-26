package com.example.recipeapppaparaproject.presentation.favoriteRecipes

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.recipeapppaparaproject.R
import com.example.recipeapppaparaproject.data.local.entity.FavoriRecipes
import com.example.recipeapppaparaproject.presentation.bottomBar.BottomBarScreen

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FavoriteRecipeScreen(
    navController: NavController,
    favViewModel: FavoriteRecipesViewModel = hiltViewModel(),
) {
    val favoriteRecipes by favViewModel.favoriteRecipes.observeAsState(emptyList())
    LaunchedEffect(Unit) {
        favViewModel.getFavoriteRecipes()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Favorite Recipes") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Image(
                painter = painterResource(id = R.drawable.foodpattern),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 25.dp)
            ) {
                if (favoriteRecipes.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No favorite recipes yet!")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(favoriteRecipes, key = { it.recipeid }) { recipe ->
                            RecipeItem(
                                recipe = recipe,
                                onRecipeClick = {
                                    recipe.recipeid.let {
                                        navController.navigate(BottomBarScreen.MealDetails.passMealId(it))
                                    }
                                },
                                onRecipeDismissed = {
                                    favViewModel.removeFavoriteRecipe(recipe.recipeid.toString())
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecipeItem(
    recipe: FavoriRecipes,
    onRecipeClick: (Int) -> Unit,
    onRecipeDismissed: (FavoriRecipes) -> Unit
) {
    val dismissState = rememberDismissState(
        confirmStateChange = { dismissValue ->
            if (dismissValue == DismissValue.DismissedToEnd || dismissValue == DismissValue.DismissedToStart) {
                onRecipeDismissed(recipe)
                true
            } else {
                false
            }
        }
    )

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
        dismissThresholds = { direction -> FractionalThreshold(0.25f) },
        background = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        dismissContent = {
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onRecipeClick(recipe.recipeid) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(8.dp)
                ) {
                    Image(
                        painter = rememberImagePainter(recipe.image),
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = recipe.title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    )
}
