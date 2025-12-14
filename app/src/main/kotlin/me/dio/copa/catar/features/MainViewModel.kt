package me.dio.copa.catar.features

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import me.dio.copa.catar.core.BaseViewModel
import me.dio.copa.catar.domain.model.MatchDomain
import me.dio.copa.catar.domain.model.TeamDomain
import me.dio.copa.catar.domain.usecase.DisableNotificationUseCase
import me.dio.copa.catar.domain.usecase.EnableNotificationUseCase
import me.dio.copa.catar.domain.usecase.GetMatchesUseCase
import me.dio.copa.catar.domain.usecase.GetTeamsUseCase
import me.dio.copa.catar.remote.NotFoundException
import me.dio.copa.catar.remote.UnexpectedException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMatchesUseCase: GetMatchesUseCase,
    private val getTeamsUseCase: GetTeamsUseCase,
    private val disableNotificationUseCase: DisableNotificationUseCase,
    private val enableNotificationUseCase: EnableNotificationUseCase,
) : BaseViewModel<MainUiState, MainUiAction>(MainUiState()) {

    init {
        fetchData()
    }

    private fun fetchData() = viewModelScope.launch {
        val matchesFlow = getMatchesUseCase()
        val teamsFlow = getTeamsUseCase()

        matchesFlow.combine(teamsFlow) { matches, teams ->
            MainUiState(matches = matches, teams = teams)
        }
            .flowOn(Dispatchers.Main)
            .catch {
                when(it) {
                    is NotFoundException ->
                        sendAction(MainUiAction.MatchesNotFound(it.message ?: "Erro sem mensagem"))
                    is UnexpectedException ->
                        sendAction(MainUiAction.Unexpected)
                }
            }.collect { state ->
                setState { state }
            }
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
    val teams: List<TeamDomain> = emptyList()
)

sealed interface MainUiAction {
    object Unexpected: MainUiAction
    data class MatchesNotFound(val message: String) : MainUiAction
    data class EnableNotification(val match: MatchDomain) : MainUiAction
    data class DisableNotification(val match: MatchDomain) : MainUiAction
}
