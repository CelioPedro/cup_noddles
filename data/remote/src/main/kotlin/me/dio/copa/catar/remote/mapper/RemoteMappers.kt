package me.dio.copa.catar.remote.mapper

import me.dio.copa.catar.domain.model.Match
import me.dio.copa.catar.domain.model.Team
import me.dio.copa.catar.remote.model.MatchDto
import me.dio.copa.catar.remote.model.TeamDto
import kotlin.jvm.JvmName

@JvmName("matchDtoToDomain")
fun List<MatchDto>.toDomain(): List<Match> = map { it.toDomain() }

fun MatchDto.toDomain() = Match(
    id = id,
    round = round ?: 0,
    stage = stage ?: "",
    date = date ?: "",
    venue = venue ?: "",
    venue_image_url = venueImageUrl ?: "",
    city = city ?: "",
    team1_id = team1_id ?: "",
    team2_id = team2_id ?: "",
    score1 = score1 ?: -1, 
    score2 = score2 ?: -1, 
    notificationEnabled = false
)

@JvmName("teamDtoToDomain")
fun List<TeamDto>.toDomain(): List<Team> = map { it.toDomain() }

fun TeamDto.toDomain() = Team(
    id = id,
    name = name ?: "",
    group = group ?: "",
    flag_url = flag_url ?: "", // Corrigido para usar o nome correto da propriedade
    ranking = ranking ?: 0
)
