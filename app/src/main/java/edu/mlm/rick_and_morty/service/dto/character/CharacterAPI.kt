package edu.mlm.rick_and_morty.service.dto.character

import android.os.Parcelable
//import edu.mlm.rick_and_morty_old.data.entity.Character
//import edu.mlm.rick_and_morty_old.data.mappers.lastSymbol
import kotlinx.parcelize.Parcelize

@Parcelize
data class CharacterAPI(
    val created: String,
    val episode: List<String>,
    val gender: String,
    val id: Int,
    val image: String,
    val location: Location,
    val name: String,
    val origin: Origin,
    val species: String,
    val status: String,
    val type: String,
    val url: String
) : Parcelable