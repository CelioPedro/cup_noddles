package me.dio.copa.catar.features

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import me.dio.copa.catar.core.BaseViewModel
import me.dio.copa.catar.domain.model.MatchDomain
import me.dio.copa.catar.domain.model.TeamDomain
import me.dio.copa.catar.domain.repositories.MatchesRepository
import me.dio.copa.catar.domain.usecase.DisableNotificationUseCase
import me.dio.copa.catar.domain.usecase.EnableNotificationUseCase
import me.dio.copa.catar.domain.usecase.GetTeamsUseCase
import me.dio.copa.catar.local.source.PreferencesManager
import me.dio.copa.catar.remote.NotFoundException
import me.dio.copa.catar.remote.UnexpectedException
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MatchesRepository,
    private val getTeamsUseCase: GetTeamsUseCase,
    private val disableNotificationUseCase: DisableNotificationUseCase,
    private val enableNotificationUseCase: EnableNotificationUseCase,
    private val preferencesManager: PreferencesManager
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
        repository.fetchAndSaveMatches()

        val teamsFlow = getTeamsUseCase()
        val favoriteTeamId = preferencesManager.getFavoriteTeamId()

        combine(
            repository.getMatches(),
            teamsFlow,
            _selectedRound
        ) { allMatches, teams, selectedRound ->
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
                        val matchTime = LocalDateTime.parse(match.date).atZone(ZoneId.systemDefault())
                        matchTime.isAfter(LocalDateTime.now().atZone(ZoneId.systemDefault()))
                    }
            } else {
                null
            }

            MainUiState(
                matches = filteredMatches,
                teams = teams,
                selectedRound = selectedRound,
                favoriteTeamMatch = favoriteTeamMatch
            )
        }
            .flowOn(Dispatchers.Main)
            .catch { exception ->
                when (exception) {
                    is NotFoundException ->
                        sendAction(MainUiAction.MatchesNotFound(exception.message ?: "Erro sem mensagem"))

                    is UnexpectedException ->
                        sendAction(MainUiAction.Unexpected)
                }
            }.collect { state ->
                setState { state }
            }
    }

    fun selectRound(round: Int) {
        _selectedRound.value = round
    }

    fun toggleNotification(match: MatchDomain) {
        viewModelScope.launch {
            runCatching {
                val action = if (match.notificationEnabled) {
                    disableNotificationUseCase(match.id.toString())
                    MainUiAction.DisableNotification(match)
                } else {
                    enableNotificationUseCase(match.id.toString())
                    MainUiAction.EnableNotification(match)
                }

                sendAction(action)
            }
        }
    }
}

data class MainUiState(
    val matches: List<MatchDomain> = emptyList(),
    val teams: List<TeamDomain> = emptyList(),
    val selectedRound: Int = 1,
    val favoriteTeamMatch: MatchDomain? = null
)

sealed interface MainUiAction {
    object Unexpected : MainUiAction
    data class MatchesNotFound(val message: String) : MainUiAction
    data class EnableNotification(val match: MatchDomain) : MainUiAction
    data class DisableNotification(val match: MatchDomain) : MainUiAction
}
