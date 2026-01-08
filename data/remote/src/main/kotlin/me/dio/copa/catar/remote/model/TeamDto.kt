package me.dio.copa.catar.remote.model

import com.squareup.moshi.Json

data class TeamDto(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "group") val group: String?,
    @field:Json(name = "flag_url") val flagUrl: String?,
    @field:Json(name = "ranking") val ranking: Int?
)
