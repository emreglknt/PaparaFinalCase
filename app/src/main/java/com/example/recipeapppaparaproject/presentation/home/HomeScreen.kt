package com.example.recipeapppaparaproject.presentation.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.recipeapppaparaproject.R
import com.example.recipeapppaparaproject.data.model.RecipeResponse.Result
import com.example.recipeapppaparaproject.nav.Screens
import com.example.recipeapppaparaproject.presentation.bottomBar.BottomBarScreen
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState

@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel = hiltViewModel()) {
    var searchText by remember { mutableStateOf("") }
    val recipeState by homeViewModel.recipeState.collectAsState()
    val categories = listOf("Breakfast","Dinner","Dessert","Pasta","Soups","Salads","Beef","Chicken")
    val isRefreshing by homeViewModel.isRefreshing.collectAsState()

    Image(
        painter = painterResource(id = R.drawable.foodpattern),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds
    )
    Scaffold(

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            SearchBar(searchText) {
                searchText = it
                homeViewModel.getRecipesByCategory(it)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Categories",
                style = MaterialTheme.typography.h6.copy(fontSize = 20.sp, fontWeight = FontWeight.Normal),
                color = Color.Black,
                modifier = Modifier
                    .padding(horizontal = 13.dp)
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    CategoryCard(category) {
                        homeViewModel.getRecipesByCategory(category)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            SwipeRefresh(
                state = SwipeRefreshState(isRefreshing),
                onRefresh = { homeViewModel.refreshRecipes() }
            ) {
                when (recipeState) {
                    is RecipeState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                    is RecipeState.Success -> {
                        val recipes = (recipeState as RecipeState.Success).recipes.results

                        if (!recipes.isNullOrEmpty()) {
                            Log.e("RecipeState", "Recipe list size: ${recipes.size}")
                            recipes.forEachIndexed { index, recipe ->
                                Log.e("RecipeState", "Recipe $index: $recipe")
                            }

                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(recipes.size) { index ->
                                    RecipeCard(recipes[index]) { recipeId ->
                                        recipeId.let {
                                            navController.navigate(BottomBarScreen.MealDetails.passMealId(it))
                                        }
                                    }
                                }
                            }
                        } else {
                            Log.e("HomeScreen", "Recipes list is empty or null")
                            Text(
                                text = "No recipes found.",
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                    is RecipeState.Error -> {
                        Text(
                            text = (recipeState as RecipeState.Error).message,
                            color = Color.Red,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun SearchBar(searchText: String, onTextChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = searchText,
                onValueChange = onTextChange,
                textStyle = TextStyle(color = Color.Black),
                singleLine = true,
                cursorBrush = SolidColor(Color.Black),
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Transparent, shape = RoundedCornerShape(16.dp))
            ) {
                if (searchText.isEmpty()) {
                    Text(
                        text = "Search recipes",
                        style = TextStyle(color = Color.Gray)
                    )
                }
                it()
            }
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 8.dp)
            )
        }
    }
}


@Composable
fun CategoryCard(category: String, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(5.dp)
            .clickable { onClick() }
            .clip(RoundedCornerShape(10.dp)) // Rounded corners
            .background(
                Brush.horizontalGradient(
                    colors = listOf(Color(0xFFFF8C61), Color(0xFF5C374C)),
                    startX = 0f,
                    endX = 1000f
                )
            )
            .size(130.dp, 50.dp)
    ) {
        Text(
            text = category,
            color = Color.White,
            fontSize = 13.sp,
            // bold text
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Composable
fun RecipeCard(recipe: Result, onClick: (Int) -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable { onClick(recipe.id) }
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
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    text = recipe.title,
                    fontSize = 15.sp,
                    color = Color.Black
                )
            }
        }
    }
}
