package com.example.recipeapppaparaproject.presentation.mealPLanScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Card
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.recipeapppaparaproject.R

import androidx.compose.material.Text
import com.example.recipeapppaparaproject.data.model.DailyMealResponse.Meal

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.drawscope.Stroke

import com.example.recipeapppaparaproject.data.model.DailyMealResponse.Nutrients



@Composable
fun MealPlanScreen(navController: NavController) {
    Image(
        painter = painterResource(id = R.drawable.foodpattern),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds
    )

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            CardCompose()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CardCompose(mealPlanViewModel: MealPlanViewModel = hiltViewModel()) {
    val mealPlanState by mealPlanViewModel.mealstate.collectAsState()

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF673AB7),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shadowElevation = 10.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.weight(2f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "What I should cook today?",
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    OutlinedButton(
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = Color(0xFFFF5733)
                        ),
                        onClick = {
                            mealPlanViewModel.getMealPlan() // Fetch data
                        }
                    ) {
                        Text(
                            text = "Get Daily Plan ➤",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.size(100.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // State-based UI updates
            when (mealPlanState) {
                is DailyMealMealState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is DailyMealMealState.Success -> {
                    val meals = (mealPlanState as DailyMealMealState.Success).recipes.meals
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(meals.size) { index ->
                            MealCard(meal = meals[index])
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    val nutrients = (mealPlanState as DailyMealMealState.Success).recipes.nutrients
                    NutrientCard(nutrients = nutrients)
                }
                is DailyMealMealState.Error -> {
                    Text(
                        text = (mealPlanState as DailyMealMealState.Error).message,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
fun MealCard(meal: Meal) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clickable { }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(8.dp)
        ) {
            Text(
                text = meal.title,
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Composable
fun NutrientCard(nutrients: Nutrients) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Nutritional Information",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NutrientBar(title = "Calories", value = nutrients.calories, color = Color.Red)
                NutrientBar(title = "Carbs", value = nutrients.carbohydrates, color = Color.Blue)
                NutrientBar(title = "Fat", value = nutrients.fat, color = Color.Yellow)
                NutrientBar(title = "Protein", value = nutrients.protein, color = Color.Green)
            }
        }
    }
}

@Composable
fun NutrientBar(title: String, value: Double, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .height(100.dp)
                .width(24.dp)
                .background(color.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp))
                .padding(top = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((value / 10).dp)
                    .background(color, shape = RoundedCornerShape(4.dp))
                    .align(Alignment.BottomCenter)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
        Text(
            text = "${value.toInt()}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}