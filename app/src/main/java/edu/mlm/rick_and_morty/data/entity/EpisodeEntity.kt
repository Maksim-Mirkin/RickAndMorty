package edu.mlm.rick_and_morty.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EpisodeEntity(
    @PrimaryKey
    val id: Int,
    val airDate: String,
    val episode: String,
    val name: String,
)