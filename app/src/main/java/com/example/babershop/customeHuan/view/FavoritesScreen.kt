package com.example.babershop.customeHuan.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun FavoritesScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
            .padding(20.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = { navController.popBackStack() }
            ) {
                Icon(Icons.Default.ArrowBack, null, tint = Color.White)
            }

            Text(
                text = "Favorites",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(modifier = Modifier.height(150.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(70.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "No favorites yet",
                color = Color.White
            )

            Text(
                "Tap the heart icon on any shop to save it",
                color = Color.Gray
            )
        }
    }
}

