package edu.mlm.rick_and_morty.service.dto.episode

import com.google.gson.annotations.SerializedName


data class EpisodeResponse(
    val info: Info,
    @SerializedName("results")
    val episodes: List<EpisodeAPI>
)