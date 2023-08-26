package edu.mlm.rick_and_morty.service.dto.location

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationAPI(
    val created: String,
    val dimension: String,
    val id: Int,
    val name: String,
    val residents: List<String>,
    val type: String,
    val url: String
) : Parcelable