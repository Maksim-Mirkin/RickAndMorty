package edu.mlm.rick_and_morty.data.mappers

import edu.mlm.rick_and_morty.data.entity.CharacterEntity
import edu.mlm.rick_and_morty.data.entity.EpisodeEntity
import edu.mlm.rick_and_morty.service.dto.character.CharacterAPI
import edu.mlm.rick_and_morty.service.dto.episode.EpisodeAPI

/**
 * Converts [CharacterAPI] to [CharacterEntity]
 */
fun CharacterAPI.toEntity() = CharacterEntity(
    id = id,
    name = name,
    status = status,
    species = species,
    gender = gender,
    type = type,
    episodeIds = episode.lastSymbol(),
    image = image,
    locationName = location.name,
    locationId = location.url.substringAfterLast('/'),
    originName = origin.name,
    originId = origin.url.substringAfterLast('/')
)

/**
 * Converts [EpisodeAPI] to [EpisodeEntity]
 */
fun EpisodeAPI.toEntity() = EpisodeEntity(
    id = id,
    airDate = airDate,
    episode = episode,
    name = name
)

/**
 * Converts list of episodes links to list of episodes IDs
 */
fun <String> List<String>.lastSymbol() = map { item ->
    val itemString = item.toString()
    itemString.substringAfterLast('/').toInt()
}