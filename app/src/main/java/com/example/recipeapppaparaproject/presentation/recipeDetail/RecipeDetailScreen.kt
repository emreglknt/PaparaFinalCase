package com.example.recipeapppaparaproject.presentation.recipeDetail

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.recipeapppaparaproject.R
import com.example.recipeapppaparaproject.data.model.RecipeDetailResponse.ExtendedIngredient
import com.example.recipeapppaparaproject.data.model.RecipeDetailResponse.RecipeDetailResponse
import com.example.recipeapppaparaproject.presentation.favoriteRecipes.FavoriteRecipesViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth


@Composable
fun RecipeDetailScreen(
    recDetailViewModel: RecipeDetailViewModel = hiltViewModel(),
    onBackMealsScreen: () -> Unit,
    favoriteRecipeViewModel: FavoriteRecipesViewModel = hiltViewModel(),
) {
    val recipeDetailState by recDetailViewModel.selectRecipe.collectAsState()
    val currentUser = Firebase.auth.currentUser

    var isFavorite by remember { mutableStateOf(false) }
// Observe favorite recipes
    val favoriteRecipes by favoriteRecipeViewModel.favoriteRecipes.observeAsState(emptyList())
    LaunchedEffect(favoriteRecipes) {
        if (recipeDetailState is RecipeDetailState.Success) {
            val recipeDetails = (recipeDetailState as RecipeDetailState.Success).meals
            isFavorite = favoriteRecipes.any { it.recipeid == recipeDetails.id && it.userId == currentUser?.uid }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Recipe Detail")
                },
                backgroundColor = Color.White,
                contentColor = Color.Black,
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (recipeDetailState) {
                is RecipeDetailState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is RecipeDetailState.Success -> {
                    val recipeDetails = (recipeDetailState as RecipeDetailState.Success).meals
                    RecipeDetailCompose(
                        recipeDetails,
                        onBackMealsScreen,
                        currentUser,
                        isFavorite,
                        favoriteRecipeViewModel
                    )
                }

                is RecipeDetailState.Error -> {
                    Text(
                        text = (recipeDetailState as RecipeDetailState.Error).message,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeDetailCompose(
    recipeDetails: RecipeDetailResponse,
    onBackMealsScreen: () -> Unit,
    currentUser: FirebaseUser?,
    isFavorite: Boolean,
    favoriteRecipeViewModel: FavoriteRecipesViewModel
) {


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        recipeDetails.let {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box {
                    Image(
                        painter = rememberImagePainter(data = recipeDetails.image),
                        contentDescription = it.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RectangleShape)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.7f)
                                    ),
                                    startY = 100f
                                )
                            )
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .align(Alignment.TopStart),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        IconButton(
                            onClick = { onBackMealsScreen() },
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = Color.Black.copy(alpha = 0.6f),
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        IconButton( // Favorite

                            onClick = { currentUser?.let { user ->
                                if (isFavorite) {
                                    favoriteRecipeViewModel.removeFavoriteRecipe(it.id.toString(), user.uid)
                                }
                                else {
                                    favoriteRecipeViewModel.addFavoriteRecipe(it, user.uid)
                                }

                                 }},
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = Color.Black.copy(alpha = 0.6f),
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (isFavorite) Color.Red else Color.White
                            )
                        }
                    }
                    Text(
                        text = it.title,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(10.dp)
                    )





                }



                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    InfoColumn(R.drawable.clock_vector, it?.cookingMinutes.toString())
                    InfoColumn(R.drawable.service_vector, it?.servings.toString())
                    InfoColumn(R.drawable.star, it?.aggregateLikes.toString())
                }


                // Ingredients text
                Text(
                    text = "Ingredients",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(15.dp),
                    color = Color.Black
                )

                // Ingredients list lazycolumn
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(recipeDetails.extendedIngredients.size) { index ->
                        val ingredient = recipeDetails.extendedIngredients[index]
                        IngredientsCard(ingredient)

                    }
                }


            }
        }

    }
}

@Composable
fun InfoColumn(@DrawableRes iconResouce: Int, text: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = iconResouce),
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.height(24.dp)
        )
        Text(text = text, fontWeight = Bold)
    }
}

@Composable
fun IngredientsCard(ingredient: ExtendedIngredient) {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = 5.dp,
        modifier = Modifier
            .width(300.dp)
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .height(60.dp)
        ) {
            AsyncImage(
                model = ingredient.image,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = ingredient.name,
                    style = MaterialTheme.typography.h6.copy(
                        fontSize = 15.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${ingredient.amount} / ${ingredient.unit}",
                    style = MaterialTheme.typography.body2.copy(
                        fontSize = 10.sp,
                    )
                )
            }
        }
    }
}
