package org.example.tictactoe

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.example.tictactoe.NavData.gamePrefs
import org.example.tictactoe.NavData.navigationOps
import org.example.tictactoe.screens.code.GameScreen
import org.example.tictactoe.screens.code.GameViewModel
import org.example.tictactoe.screens.code.HomeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = navigationOps.home){

        composable(navigationOps.home){
            HomeScreen(navController = navController)
        }

        composable("${navigationOps.gamePage}/{${gamePrefs.gameMode}}"){ backStackEntry ->
            val gameMode : String? = backStackEntry.arguments?.getString(gamePrefs.gameMode)
            GameScreen(navController = navController, mode = gameMode, viewModel = GameViewModel())
        }
    }
}



