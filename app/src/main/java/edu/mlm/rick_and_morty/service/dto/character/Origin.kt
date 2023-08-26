package edu.mlm.rick_and_morty.service.dto.character


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Origin(
    val name: String,
    val url: String
) : Parcelable