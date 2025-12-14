package me.dio.copa.catar.features

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import me.dio.copa.catar.R
import me.dio.copa.catar.domain.model.MatchDomain
import me.dio.copa.catar.domain.model.TeamDomain
import me.dio.copa.catar.ui.theme.Shapes
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

typealias NotificationOnClick = (match: MatchDomain) -> Unit

@Composable
fun MainScreen(
    matches: List<MatchDomain>,
    teams: List<TeamDomain>,
    onToggleNotification: NotificationOnClick
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(matches) { match ->
                val team1 = teams.find { it.id == match.team1_id }
                val team2 = teams.find { it.id == match.team2_id }

                if (team1 != null && team2 != null) {
                    MatchInfo(match, team1, team2, onToggleNotification)
                }
            }
        }
    }
}

@Composable
fun MatchInfo(
    match: MatchDomain,
    team1: TeamDomain,
    team2: TeamDomain,
    onToggleNotification: NotificationOnClick
) {
    Card(
        shape = Shapes.large,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(color = MaterialTheme.colors.surface.copy(alpha = 0.5f)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Notification(match, onToggleNotification)
                Title(match)
                Teams(team1, team2)
            }
        }
    }
}

@Composable
fun Notification(match: MatchDomain, onClick: NotificationOnClick) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        val drawable = if (match.notificationEnabled) R.drawable.ic_notifications_active
        else R.drawable.ic_notifications

        Image(
            painter = painterResource(id = drawable),
            modifier = Modifier.clickable {
                onClick(match)
            },
            contentDescription = null
        )
    }
}

@Composable
fun Title(match: MatchDomain) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM HH:mm")
    val date = LocalDateTime.parse(match.date, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "${date.format(dateFormatter)} - ${match.stage}",
            style = MaterialTheme.typography.h6.copy(color = Color.White)
        )
    }
}

@Composable
fun Teams(team1: TeamDomain, team2: TeamDomain) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TeamItem(team = team1)

        Text(
            text = "X",
            modifier = Modifier.padding(end = 16.dp, start = 16.dp),
            style = MaterialTheme.typography.h6.copy(color = Color.White)
        )

        TeamItem(team = team2)
    }
}

@Composable
fun TeamItem(team: TeamDomain) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            model = team.flag_url,
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            text = team.name,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6.copy(color = Color.White)
        )
    }
}
