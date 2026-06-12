package com.example.barberapp.View.screenUI.customer.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.barberapp.View.component.EmptyStateLabel
import com.example.barberapp.View.utils.BackgroundColor
import com.example.barberapp.View.utils.OnSurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavController,
) {
    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Favorites",
                        color = OnSurface,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = OnSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundColor
                )
            )
        }
    ) { innerPadding ->
        EmptyStateLabel(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            imageVector = Icons.Default.FavoriteBorder,
            primaryMessage = "No favorites yet",
            hintMessage = "Tap the heart icon on any shop to save it"
        )
    }
}

// ── Preview ───────────────────────────────────────────────────
//@Preview(
//    name = "Favorites – Empty State",
//    showBackground = true,
//    backgroundColor = 0xFF121212,
//    showSystemUi = true
//)
//@Composable
//private fun FavoritesScreenPreview() {
//    FavoritesScreen()
//}