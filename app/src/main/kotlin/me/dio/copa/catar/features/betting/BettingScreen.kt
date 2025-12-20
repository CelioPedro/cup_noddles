package me.dio.copa.catar.features.betting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import me.dio.copa.catar.domain.model.MatchDomain
import me.dio.copa.catar.domain.model.TeamDomain

@Composable
fun BettingScreen(viewModel: BettingViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        val matches = uiState.matches
        items(
            count = matches.size,
            key = { index -> matches[index].id } // Usar uma chave estável é uma boa prática
        ) { index ->
            val match = matches[index]
            val team1 = uiState.teams.find { it.id == match.team1_id }
            val team2 = uiState.teams.find { it.id == match.team2_id }

            if (team1 != null && team2 != null) {
                val bet = uiState.bets[match.id.toString()]
                BettingMatchItem(
                    match = match,
                    team1 = team1,
                    team2 = team2,
                    bet = bet,
                    onBetChanged = { score1, score2 ->
                        viewModel.onBetChanged(match.id.toString(), score1, score2)
                    }
                )
            }
        }
    }
}

@Composable
fun BettingMatchItem(
    match: MatchDomain,
    team1: TeamDomain,
    team2: TeamDomain,
    bet: Pair<String, String>?,
    onBetChanged: (score1: String, score2: String) -> Unit
) {
    val score1 = bet?.first ?: ""
    val score2 = bet?.second ?: ""

    Card {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = "${match.date} - ${match.stage}",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = team1.name, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                OutlinedTextField(
                    value = score1,
                    onValueChange = { onBetChanged(it, score2) },
                    modifier = Modifier.width(60.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Text(text = "X", modifier = Modifier.padding(horizontal = 16.dp))
                OutlinedTextField(
                    value = score2,
                    onValueChange = { onBetChanged(score1, it) },
                    modifier = Modifier.width(60.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Text(text = team2.name, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
            }
        }
    }
}
