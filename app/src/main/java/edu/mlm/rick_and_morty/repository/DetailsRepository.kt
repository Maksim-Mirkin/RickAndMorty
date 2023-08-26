package edu.mlm.rick_and_morty.repository

import edu.mlm.rick_and_morty.service.RAMService
import edu.mlm.rick_and_morty.service.dto.character.CharacterAPI
import edu.mlm.rick_and_morty.service.dto.episode.EpisodeAPI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class DetailsRepository @Inject constructor(private val service: RAMService) {

    /**
     * A list of [EpisodeAPI] for user display on various details screens.
     */
    private val episodes = MutableStateFlow(listOf<EpisodeAPI>())

    /**
     * A list of [CharacterAPI] for user display on various details screens.
     */
    private val characters = MutableStateFlow(listOf<CharacterAPI>())

    fun getCharacters() = characters
    fun setCharacters(characters: List<CharacterAPI>) {
        this.characters.value = characters
    }

    fun getEpisodes() = episodes
    fun setEpisodes(episodes: List<EpisodeAPI>) {
        this.episodes.value = episodes
    }

    /**
     * Retrieves a list of episodes as links, extracts the ID from each link, makes an API request
     * to get the episode by ID, and adds it to the final returned list.
     */
    suspend fun getEpisodeList(episodes: List<String>): List<EpisodeAPI> {
        val episodeItems: MutableList<EpisodeAPI> = mutableListOf()
        episodes.map { episodeUrl ->
            val episodeId = episodeUrl.substringAfterLast('/').toInt()
            episodeItems.add(service.getEpisode(episodeId))
        }
        return episodeItems
    }

    /**
     * Retrieves a list of characters as links, extracts the ID from each link, makes an API request
     * to get the character by ID, and adds it to the final returned list.
     */
    suspend fun getCharacterList(characters: List<String>): List<CharacterAPI> {
        val characterItems: MutableList<CharacterAPI> = mutableListOf()
        characters.map { item ->
            val characterId = item.substringAfterLast('/').toInt()
            characterItems.add(service.getCharacter(characterId))
        }
        return characterItems
    }

    /**
     * Makes API request to get location by [id]
     */
    suspend fun getLocation(id: Int) = service.getLocation(id)

    /**
     * Makes API request to get episode by [id]
     */
    suspend fun getEpisode(id: Int) = service.getEpisode(id)
}
