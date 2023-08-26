package edu.mlm.rick_and_morty.service.dto.character

import com.google.gson.annotations.SerializedName
import edu.mlm.rick_and_morty.service.dto.character.CharacterAPI
import edu.mlm.rick_and_morty.service.dto.character.Info


data class CharacterResponse(
    val info: Info,
    @SerializedName("results")
    val characters: List<CharacterAPI>
)