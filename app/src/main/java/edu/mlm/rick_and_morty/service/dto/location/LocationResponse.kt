package edu.mlm.rick_and_morty.service.dto.location

import com.google.gson.annotations.SerializedName
import edu.mlm.rick_and_morty.service.dto.location.Info
import edu.mlm.rick_and_morty.service.dto.location.LocationAPI


data class LocationResponse(
    val info: Info,
    @SerializedName("results")
    val locations: List<LocationAPI>
)