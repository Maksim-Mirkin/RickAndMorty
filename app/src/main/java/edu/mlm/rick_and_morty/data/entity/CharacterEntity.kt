package edu.mlm.rick_and_morty.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class CharacterEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val type: String,
    val episodeIds: List<Int>,
    val image: String,
    val locationName: String,
    val locationId: String,
    val originName: String,
    val originId: String,
) : Parcelable
