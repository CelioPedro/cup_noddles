package me.dio.copa.catar.data.mapper

import me.dio.copa.catar.local.model.MatchEntity
import me.dio.copa.catar.local.model.TeamEntity
import me.dio.copa.catar.remote.model.MatchDto
import me.dio.copa.catar.remote.model.TeamDto
import me.dio.copa.catar.domain.model.Match
import me.dio.copa.catar.domain.model.Team
import kotlin.jvm.JvmName

// DTO para Entidade: Apenas converte os tipos, tratando nulos com valores padrão.
fun MatchDto.toEntity() = MatchEntity(
    id = id,
    round = round ?: 0,
    stage = stage ?: "",
    date = date ?: "",
    venue = venue ?: "",
    venueImageUrl = venueImageUrl ?: "",
    city = city ?: "",
    team1Id = team1_id ?: "", // Corrigido para usar o nome correto da propriedade
    team2Id = team2_id ?: "", // Corrigido para usar o nome correto da propriedade
    score1 = score1 ?: -1, 
    score2 = score2 ?: -1
)

fun TeamDto.toEntity() = TeamEntity(
    id = id,
    name = name ?: "",
    group = group ?: "",
    flagUrl = flagUrl ?: "",
    ranking = ranking ?: 0
)

// Entidade para Domínio: Uma conversão direta. A UI é responsável por lidar com os dados.
fun MatchEntity.toDomain() = Match(
    id = id,
    round = round,
    stage = stage,
    date = date,
    venue = venue,
    venue_image_url = venueImageUrl,
    city = city,
    team1_id = team1Id,
    team2_id = team2Id,
    score1 = score1,
    score2 = score2,
    notificationEnabled = notificationEnabled
)

fun TeamEntity.toDomain() = Team(
    id = id,
    name = name,
    group = group,
    flag_url = flagUrl,
    ranking = ranking
)

// Revertido para um map simples, sem descartar nenhuma partida.
@JvmName("matchEntityToDomain")
fun List<MatchEntity>.toDomain(): List<Match> = map { it.toDomain() }

@JvmName("teamEntityToDomain")
fun List<TeamEntity>.toDomain(): List<Team> = map { it.toDomain() }
