package me.dio.copa.catar.features.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.dio.copa.catar.domain.model.TeamDomain
import me.dio.copa.catar.domain.repositories.TeamsRepository
import me.dio.copa.catar.domain.usecase.GetTeamsUseCase
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val getTeamsUseCase: GetTeamsUseCase,
    private val repository: TeamsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    init {
        fetchTeams()
    }

    private fun fetchTeams() {
        viewModelScope.launch {
            try {
                // Apenas a chamada de rede, que pode falhar, fica no Dispatchers.IO
                withContext(Dispatchers.IO) {
                    repository.sync()
                }

                // Se sync() for bem-sucedido, buscamos os dados do banco e atualizamos a UI.
                // Usamos .first() para pegar apenas o primeiro resultado do Flow, evitando múltiplas emissões.
                val teams = getTeamsUseCase().first()
                _uiState.update { it.copy(teams = teams, error = null) } // Limpa qualquer erro anterior

            } catch (e: Exception) {
                // Se sync() falhar, o erro é capturado e a UI é atualizada com uma mensagem clara.
                _uiState.update { it.copy(error = "Falha ao buscar dados: ${e.javaClass.simpleName}") }
            }
        }
    }

    fun selectTeam(teamId: String) {
        _uiState.update { it.copy(selectedTeamId = teamId) }
    }
}

data class OnboardingUiState(
    val teams: List<TeamDomain> = emptyList(),
    val selectedTeamId: String? = null,
    val error: String? = null
)
