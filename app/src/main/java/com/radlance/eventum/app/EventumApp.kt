package com.radlance.eventum.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.radlance.eventum.navigation.base.NavGraph

@Composable
fun EventumApp(modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier.fillMaxSize()) { paddingValues ->
        val navHostController = rememberNavController()
        NavGraph(navController = navHostController, paddingValues = paddingValues)
    }
}