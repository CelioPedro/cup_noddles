package me.dio.copa.catar.features

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.dio.copa.catar.core.BaseViewModel
import me.dio.copa.catar.domain.model.MatchDomain
import me.dio.copa.catar.domain.model.TeamDomain
import me.dio.copa.catar.domain.repositories.MatchesRepository
import me.dio.copa.catar.domain.repositories.TeamsRepository
import me.dio.copa.catar.domain.usecase.GetMatchesUseCase
import me.dio.copa.catar.domain.usecase.GetTeamsUseCase
import me.dio.copa.catar.domain.usecase.ToggleNotificationUseCase
import me.dio.copa.catar.local.source.PreferencesManager
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMatchesUseCase: GetMatchesUseCase,
    private val getTeamsUseCase: GetTeamsUseCase,
    private val toggleNotificationUseCase: ToggleNotificationUseCase,
    private val preferencesManager: PreferencesManager,
    private val matchesRepository: MatchesRepository,
    private val teamsRepository: TeamsRepository
) : BaseViewModel<MainUiState, MainUiAction>(MainUiState()) {

    private val _selectedRound = MutableStateFlow(1)

    private val rounds = listOf(
        "Rodada 1", "Rodada 2", "Rodada 3", "16 avos",
        "Oitavas", "Quartas", "Semi", "Final"
    )

    init {
        fetchData()
    }

    private fun fetchData() = viewModelScope.launch {
        try {
            withContext(Dispatchers.IO) {
                val matchesDeferred = async { matchesRepository.sync() }
                val teamsDeferred = async { teamsRepository.sync() }
                matchesDeferred.await()
                teamsDeferred.await()
            }

            val favoriteTeamId = preferencesManager.getFavoriteTeamId()

            combine(
                getMatchesUseCase(),
                getTeamsUseCase(),
                _selectedRound
            ) { allMatches, allTeams, selectedRound ->
                
                // Verificação de depuração: força um erro se a lista de times estiver vazia.
                if (allTeams.isEmpty()) {
                    throw IllegalStateException("A lista de times está vazia após a sincronização.")
                }

                val filteredMatches = when (rounds[selectedRound - 1]) {
                    "16 avos" -> allMatches.filter { it.stage == "16 avos de final" }
                    "Oitavas" -> allMatches.filter { it.stage == "Oitavas de Final" }
                    "Quartas" -> allMatches.filter { it.stage == "Quartas de Final" }
                    "Semi" -> allMatches.filter { it.stage == "Semifinal" }
                    "Final" -> allMatches.filter { it.stage == "Final" || it.stage == "Terceiro Lugar" }
                    else -> allMatches.filter { it.round == selectedRound }
                }

                val favoriteTeamMatch = if (favoriteTeamId != null) {
                    allMatches.filter { it.team1_id == favoriteTeamId || it.team2_id == favoriteTeamId }
                        .firstOrNull { match ->
                            val matchTime = match.date.takeIf { it.isNotBlank() }?.let { LocalDateTime.parse(it).atZone(ZoneId.systemDefault()) }
                            matchTime?.isAfter(LocalDateTime.now().atZone(ZoneId.systemDefault())) ?: false
                        }
                } else {
                    null
                }

                MainUiState(
                    matches = filteredMatches,
                    teams = allTeams,
                    selectedRound = selectedRound,
                    favoriteTeamMatch = favoriteTeamMatch,
                    error = null
                )
            }
                .catch { exception ->
                    setState { copy(error = "Falha ao carregar dados: ${exception.message}") }
                }.collect { state ->
                    setState { state }
                }
        } catch (e: Exception) {
            setState { copy(error = "Falha ao sincronizar dados: ${e.message}") }
        }
    }

    fun selectRound(round: Int) {
        _selectedRound.value = round
    }

    fun toggleNotification(match: MatchDomain) {
        viewModelScope.launch {
            runCatching {
                toggleNotificationUseCase(match.id)
                sendAction(MainUiAction.ToggleNotification(match))
            }
        }
    }
}


data class MainUiState(
    val matches: List<MatchDomain> = emptyList(),
    val teams: List<TeamDomain> = emptyList(),
    val selectedRound: Int = 1,
    val favoriteTeamMatch: MatchDomain? = null,
    val error: String? = null
)

sealed interface MainUiAction {
    object Unexpected : MainUiAction
    data class MatchesNotFound(val message: String) : MainUiAction
    data class ToggleNotification(val match: MatchDomain) : MainUiAction
}
