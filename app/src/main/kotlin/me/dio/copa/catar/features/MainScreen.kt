package me.dio.copa.catar.features

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.dio.copa.catar.ui.components.AppBottomNavigation
import me.dio.copa.catar.ui.navigation.NavigationItem

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val viewModel: MainViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    Scaffold(
        bottomBar = { AppBottomNavigation(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavigationItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavigationItem.Home.route) {
                HomeScreen(viewModel = viewModel)
            }
            composable(NavigationItem.Rounds.route) {
                RoundsScreen(
                    matches = state.matches,
                    teams = state.teams,
                    selectedRound = state.selectedRound,
                    onSelectRound = viewModel::selectRound,
                    onToggleNotification = viewModel::toggleNotification
                )
            }
            composable(NavigationItem.Betting.route) {
                BettingScreen()
            }
            composable(NavigationItem.Profile.route) {
                ProfileScreen()
            }
        }
    }
}