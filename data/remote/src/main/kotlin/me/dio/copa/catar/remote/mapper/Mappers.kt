package me.dio.copa.catar.remote.mapper

import me.dio.copa.catar.domain.model.MatchDomain
import me.dio.copa.catar.domain.model.TeamDomain
import me.dio.copa.catar.remote.model.MatchRemote
import me.dio.copa.catar.remote.model.TeamRemote

fun List<MatchRemote>.toMatchDomain(): List<MatchDomain> = map(MatchRemote::toDomain)

fun MatchRemote.toDomain(): MatchDomain = MatchDomain(
    id = id,
    stage = stage,
    date = date,
    venue = venue,
    city = city,
    team1_id = team1Id,
    team2_id = team2Id,
    score1 = score1,
    score2 = score2
)

fun List<TeamRemote>.toTeamDomain(): List<TeamDomain> = map(TeamRemote::toDomain)

fun TeamRemote.toDomain(): TeamDomain = TeamDomain(
    id = id,
    name = name,
    group = group,
    flag_url = flagUrl,
    ranking = ranking
)
