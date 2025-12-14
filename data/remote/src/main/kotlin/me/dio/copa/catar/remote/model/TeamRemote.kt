package me.dio.copa.catar.remote.model

import com.google.gson.annotations.SerializedName

typealias TeamRemote = Team

data class Team(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("group") val group: String,
    @SerializedName("flag_url") val flagUrl: String,
    @SerializedName("ranking") val ranking: Int
)
