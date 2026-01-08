package me.dio.copa.catar.features.betting

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
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
    val totalPoints = uiState.matches.sumOf { match ->
        val bet = uiState.bets[match.id.toString()]
        getPoints(match, bet)
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        item {
            Text(text = "Total de pontos: $totalPoints")
        }

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
                    },
                    onLongClick = {}
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BettingMatchItem(
    match: MatchDomain,
    team1: TeamDomain,
    team2: TeamDomain,
    bet: Pair<String, String>?,
    onBetChanged: (score1: String, score2: String) -> Unit,
    onLongClick: () -> Unit
) {
    val score1 = bet?.first ?: ""
    val score2 = bet?.second ?: ""
    val points = getPoints(match, bet)

    Card(
        modifier = Modifier.combinedClickable(
            onLongClick = onLongClick,
            onClick = {}
        )
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "${match.date} - ${match.stage}")
                if (points > 0) {
                    Text(text = " ($points pontos)")
                }
            }

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

            if (match.score1 != null && match.score2 != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Placar: ${match.score1} X ${match.score2}")
                }
            }
        }
    }
}

private fun getPoints(match: MatchDomain, bet: Pair<String, String>?): Int {
    val (bet1, bet2) = bet?.first?.toIntOrNull() to bet?.second?.toIntOrNull()
    val (score1, score2) = match.score1 to match.score2

    if (score1 == null || score2 == null) return 0
    if (bet1 == null || bet2 == null) return 0

    if (bet1 == score1 && bet2 == score2) {
        return 3
    }

    val matchResult = when {
        score1 > score2 -> 1
        score1 < score2 -> 2
        else -> 0
    }

    val betResult = when {
        bet1 > bet2 -> 1
        bet1 < bet2 -> 2
        else -> 0
    }

    if (matchResult == betResult) {
        return 1
    }

    return 0
}
