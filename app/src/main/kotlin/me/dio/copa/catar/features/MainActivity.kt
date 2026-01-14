package me.dio.copa.catar.features

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dagger.hilt.android.AndroidEntryPoint
import me.dio.copa.catar.extensions.observe
import me.dio.copa.catar.features.onboarding.OnboardingScreen
import me.dio.copa.catar.features.onboarding.OnboardingViewModel
import me.dio.copa.catar.notification.scheduler.extensions.NotificationMatcherWorker
import me.dio.copa.catar.ui.theme.Copa2022Theme

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
                    MainScreen()
                }
            }
        }
    }

    private fun observeActions() {
        mainViewModel.action.observe(this) { action ->
            when (action) {
                is MainUiAction.MatchesNotFound -> TODO()
                MainUiAction.Unexpected -> TODO()
                is MainUiAction.ToggleNotification -> {
                    if (action.match.notificationEnabled) {
                        NotificationMatcherWorker.cancel(applicationContext, action.match)
                    } else {
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
}