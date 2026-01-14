package me.dio.copa.catar.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import me.dio.copa.catar.domain.model.MatchDomain
import me.dio.copa.catar.domain.model.TeamDomain
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun MatchInfo(
    match: MatchDomain,
    team1: TeamDomain,
    team2: TeamDomain,
    onToggleNotification: (MatchDomain) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                TeamScore(team = team1, score = match.score1)
                Spacer(modifier = Modifier.height(12.dp))
                TeamScore(team = team2, score = match.score2)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = match.date.toFormattedDate(),
                    style = MaterialTheme.typography.caption
                )
            }

            IconToggleButton(
                checked = match.notificationEnabled,
                onCheckedChange = { onToggleNotification(match) }
            ) {
                Icon(
                    imageVector = if (match.notificationEnabled) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                    contentDescription = "Toggle Notification"
                )
            }
        }
    }
}

@Composable
private fun TeamScore(team: TeamDomain, score: Int?) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(team.flag_url)
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = team.name, style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = score?.toString() ?: "-", style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold))
    }
}

private fun String.toFormattedDate(): String {
    val odt = LocalDateTime.parse(this)
    return DateTimeFormatter.ofPattern("dd/MM HH:mm").format(odt)
}
