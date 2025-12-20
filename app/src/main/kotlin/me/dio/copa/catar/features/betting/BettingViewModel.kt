package me.dio.copa.catar.features.betting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import me.dio.copa.catar.domain.model.MatchDomain
import me.dio.copa.catar.domain.model.TeamDomain
import me.dio.copa.catar.domain.usecase.GetBetsUseCase
import me.dio.copa.catar.domain.usecase.GetMatchesUseCase
import me.dio.copa.catar.domain.usecase.GetTeamsUseCase
import me.dio.copa.catar.domain.usecase.SaveBetUseCase
import javax.inject.Inject

@HiltViewModel
class BettingViewModel @Inject constructor(
    private val getMatchesUseCase: GetMatchesUseCase,
    private val getTeamsUseCase: GetTeamsUseCase,
    private val getBetsUseCase: GetBetsUseCase,
    private val saveBetUseCase: SaveBetUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BettingUiState())
    val uiState: StateFlow<BettingUiState> = _uiState.asStateFlow()

    init {
        fetchData()
    }

    fun onBetChanged(matchId: String, score1: String, score2: String) {
        viewModelScope.launch {
            saveBetUseCase(matchId, score1, score2)
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            val matchesFlow = getMatchesUseCase()
            val teamsFlow = getTeamsUseCase()
            val betsFlow = getBetsUseCase()

            combine(matchesFlow, teamsFlow, betsFlow) { matches, teams, bets ->
                BettingUiState(matches = matches, teams = teams, bets = bets)
            }.collect { 
                _uiState.value = it
            }
        }
    }
}

data class BettingUiState(
    val matches: List<MatchDomain> = emptyList(),
    val teams: List<TeamDomain> = emptyList(),
    val bets: Map<String, Pair<String, String>> = emptyMap()
)
