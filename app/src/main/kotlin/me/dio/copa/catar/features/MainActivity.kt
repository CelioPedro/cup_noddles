package me.dio.copa.catar.features

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import me.dio.copa.catar.extensions.observe
import me.dio.copa.catar.features.betting.BettingScreen
import me.dio.copa.catar.features.onboarding.OnboardingScreen
import me.dio.copa.catar.features.onboarding.OnboardingViewModel
import me.dio.copa.catar.notification.scheduler.extensions.NotificationMatcherWorker
import me.dio.copa.catar.ui.theme.Copa2022Theme

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Início", Icons.Default.Home)
    object Betting : Screen("betting", "Bolão", Icons.Default.Star)
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    private val onboardingViewModel by viewModels<OnboardingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeActions()

        val prefs = getSharedPreferences("copa_prefs", Context.MODE_PRIVATE)
        val hasCompletedOnboarding = prefs.getBoolean("onboarding_completed", false)

        setContent {
            Copa2022Theme {
                var showOnboarding by remember { mutableStateOf(!hasCompletedOnboarding) }

                if (showOnboarding) {
                    OnboardingScreen(
                        viewModel = onboardingViewModel,
                        onOnboardingCompleted = { teamId ->
                            prefs.edit()
                                .putBoolean("onboarding_completed", true)
                                .putString("favorite_team_id", teamId)
                                .apply()
                            showOnboarding = false
                        }
                    )
                } else {
                    AppNavigation(mainViewModel)
                }
            }
        }
    }

    private fun observeActions() {
        mainViewModel.action.observe(this) { action ->
            when (action) {
                is MainUiAction.MatchesNotFound -> TODO()
                MainUiAction.Unexpected -> TODO()
                is MainUiAction.DisableNotification ->
                    NotificationMatcherWorker.cancel(applicationContext, action.match)
                is MainUiAction.EnableNotification -> {
                    val state = mainViewModel.state.value
                    val team1 = state.teams.find { it.id == action.match.team1_id }
                    val team2 = state.teams.find { it.id == action.match.team2_id }

                    if (team1 != null && team2 != null) {
                        NotificationMatcherWorker.start(applicationContext, action.match, team1, team2)
                    }
                }
            }
        }
    }
}

@Composable
private fun AppNavigation(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val items = listOf(Screen.Home, Screen.Betting)

    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Home.route, Modifier.padding(innerPadding)) {
            composable(Screen.Home.route) {
                val state by viewModel.state.collectAsState()
                MainScreen(
                    matches = state.matches,
                    teams = state.teams,
                    selectedRound = state.selectedRound,
                    favoriteTeamMatch = state.favoriteTeamMatch,
                    onToggleNotification = viewModel::toggleNotification,
                    onSelectRound = viewModel::selectRound
                )
            }
            composable(Screen.Betting.route) {
                BettingScreen()
            }
        }
    }
}
