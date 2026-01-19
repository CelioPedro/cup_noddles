package me.dio.copa.catar.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import me.dio.copa.catar.domain.model.MatchDomain
import me.dio.copa.catar.domain.model.TeamDomain
import me.dio.copa.catar.domain.usecase.GetMatchesUseCase
import me.dio.copa.catar.domain.usecase.GetTeamsUseCase
import javax.inject.Inject

@HiltViewModel
class RoundsViewModel @Inject constructor(
    private val getMatchesUseCase: GetMatchesUseCase,
    private val getTeamsUseCase: GetTeamsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoundsUiState())
    val uiState = _uiState.asStateFlow()

    private val _selectedRound = MutableStateFlow(1)

    private val rounds = listOf(
        "Rodada 1", "Rodada 2", "Rodada 3", "16 avos",
        "Oitavas", "Quartas", "Semi", "Final"
    )

    init {
        fetchContent()
    }

    private fun fetchContent() {
        viewModelScope.launch {
            combine(
                getMatchesUseCase(),
                getTeamsUseCase(),
                _selectedRound
            ) { allMatches, allTeams, selectedRound ->

                val filteredMatches = when (rounds.getOrNull(selectedRound - 1)) {
                    "Rodada 1" -> allMatches.filter { it.round == 1 }
                    "Rodada 2" -> allMatches.filter { it.round == 2 }
                    "Rodada 3" -> allMatches.filter { it.round == 3 }
                    "16 avos" -> allMatches.filter { it.stage.equals("16 avos de final", ignoreCase = true) }
                    "Oitavas" -> allMatches.filter { it.stage.equals("Oitavas de Final", ignoreCase = true) }
                    "Quartas" -> allMatches.filter { it.stage.equals("Quartas de Final", ignoreCase = true) }
                    "Semi" -> allMatches.filter { it.stage.equals("Semifinal", ignoreCase = true) }
                    "Final" -> allMatches.filter { it.stage.equals("Final", ignoreCase = true) || it.stage.equals("Terceiro Lugar", ignoreCase = true) }
                    else -> emptyList()
                }

                RoundsUiState(
                    matches = filteredMatches,
                    teams = allTeams,
                    selectedRound = selectedRound
                )
            }.catch { exception ->
                _uiState.value = _uiState.value.copy(error = exception.message)
            }.collect { combinedState ->
                _uiState.value = combinedState
            }
        }
    }

    fun selectRound(round: Int) {
        _selectedRound.value = round
    }
}

data class RoundsUiState(
    val matches: List<MatchDomain> = emptyList(),
    val teams: List<TeamDomain> = emptyList(),
    val selectedRound: Int = 1,
    val error: String? = null
)
