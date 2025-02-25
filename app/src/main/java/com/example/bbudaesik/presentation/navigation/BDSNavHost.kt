package com.example.bbudaesik.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bbudaesik.presentation.ui.mainscreen.MainScreen

@Composable
fun BDSNavHost(innerPadding: PaddingValues) {
    val navController = rememberNavController()
    NavHost(
        navController,
        startDestination = "mainScreen",
        modifier = Modifier.padding(innerPadding)) {
        composable("mainScreen") {
            MainScreen(navController)
        }
    }
}
