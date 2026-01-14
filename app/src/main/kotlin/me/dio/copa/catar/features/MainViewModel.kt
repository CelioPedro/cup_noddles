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
import me.dio.copa.catar.domain.usecase.GetMatchesUseCase
import me.dio.copa.catar.domain.usecase.GetTeamsUseCase
import me.dio.copa.catar.domain.usecase.SyncMatchesUseCase
import me.dio.copa.catar.domain.usecase.SyncTeamsUseCase
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
    private val syncMatchesUseCase: SyncMatchesUseCase,
    private val syncTeamsUseCase: SyncTeamsUseCase
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
        setState { copy(isLoading = true) }
        try {
            withContext(Dispatchers.IO) {
                val matchesDeferred = async { syncMatchesUseCase() }
                val teamsDeferred = async { syncTeamsUseCase() }
                matchesDeferred.await()
                teamsDeferred.await()
            }

            val favoriteTeamId = preferencesManager.getFavoriteTeamId()

            combine(
                getMatchesUseCase(),
                getTeamsUseCase(),
                _selectedRound
            ) { allMatches, allTeams, selectedRound ->

                if (allTeams.isEmpty()) {
                    throw IllegalStateException("A lista de times está vazia após a sincronização.")
                }

                val filteredMatches = when (rounds[selectedRound - 1]) {
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
                    isLoading = false,
                    error = null
                )
            }
                .catch { exception ->
                    setState { copy(isLoading = false, error = "Falha ao carregar dados: ${exception.message}") }
                }.collect { state ->
                    setState { state }
                }
        } catch (e: Exception) {
            setState { copy(isLoading = false, error = "Falha ao sincronizar dados: ${e.message}") }
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
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface MainUiAction {
    object Unexpected : MainUiAction
    data class MatchesNotFound(val message: String) : MainUiAction
    data class ToggleNotification(val match: MatchDomain) : MainUiAction
}
