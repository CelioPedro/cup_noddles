package me.dio.copa.catar.features.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.dio.copa.catar.domain.model.TeamDomain
import me.dio.copa.catar.domain.usecase.GetTeamsUseCase
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val getTeamsUseCase: GetTeamsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    init {
        fetchTeams()
    }

    private fun fetchTeams() {
        viewModelScope.launch {
            getTeamsUseCase().collect { teams ->
                _uiState.update { it.copy(teams = teams) }
            }
        }
    }

    fun selectTeam(teamId: String) {
        _uiState.update { it.copy(selectedTeamId = teamId) }
    }
}

data class OnboardingUiState(
    val teams: List<TeamDomain> = emptyList(),
    val selectedTeamId: String? = null
)
