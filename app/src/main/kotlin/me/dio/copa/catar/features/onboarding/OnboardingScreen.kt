package me.dio.copa.catar.features.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import me.dio.copa.catar.domain.model.Team

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    onOnboardingCompleted: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Qual sua seleção favorita?",
            style = MaterialTheme.typography.h5
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(uiState.teams) { team ->
                TeamItem(team = team, selected = team.id == uiState.selectedTeamId) {
                    viewModel.selectTeam(team.id)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { uiState.selectedTeamId?.let(onOnboardingCompleted) },
            enabled = uiState.selectedTeamId != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("PROSSEGUIR")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun TeamItem(team: Team, selected: Boolean, onClick: () -> Unit) {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components { add(SvgDecoder.Factory()) }
        .build()
        
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected, 
            onClick = onClick
        )

        Spacer(modifier = Modifier.size(16.dp))
        
        if (team.flag_url.isNotBlank()) {
            AsyncImage(
                model = team.flag_url,
                contentDescription = "Bandeira do ${team.name}",
                imageLoader = imageLoader,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.size(16.dp))
        }

        Text(text = team.name)
    }
}
