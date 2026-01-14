package me.dio.copa.catar.features

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.dio.copa.catar.ui.components.MatchInfo
import me.dio.copa.catar.ui.components.NewsCard

@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val state by viewModel.state.collectAsState()

    if (state.isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else if (state.error != null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = state.error ?: "Ocorreu um erro")
        }
    } else {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            state.favoriteTeamMatch?.let { match ->
                val team1 = state.teams.find { it.id == match.team1_id }
                val team2 = state.teams.find { it.id == match.team2_id }

                if (team1 != null && team2 != null) {
                    item {
                        MatchInfo(
                            match = match,
                            team1 = team1,
                            team2 = team2,
                            onToggleNotification = { viewModel.toggleNotification(match) }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            // Feed de notícias criativo
            item { NewsCard("Análise Tática", "Brasil mostra força e vence a Sérvia por 2x0. Richarlison brilha com dois gols, sendo um deles uma pintura.") }
            item { NewsCard("Jogador da Rodada", "Com uma atuação de gala, Messi comanda a vitória da Argentina contra o México.") }
            item { NewsCard("Curiosidade Histórica", "Você sabia? A primeira Copa do Mundo foi realizada em 1930 no Uruguai.") }
        }
    }
}
