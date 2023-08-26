package edu.mlm.rick_and_morty.repository

import edu.mlm.rick_and_morty.service.RAMService
import edu.mlm.rick_and_morty.service.dto.episode.EpisodeAPI
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class EpisodeRepository @Inject constructor(private val service: RAMService) :
    Repository<EpisodeAPI>(service) {

    /**
     * Container for episodes name filter.
     */
    private val name = MutableStateFlow("")

    /**
     * Container for seasons number filter.
     */
    private val seasonNumber = MutableStateFlow("")

    /**
     * Container for episodes number filter.
     */
    private val episodeNumber = MutableStateFlow("")

    /**
     * A list of [EpisodeAPI] for user display on various screens.
     */
    private val episodes = MutableStateFlow(listOf<EpisodeAPI>())

    /**
     * Makes API request using [name], [seasonNumber] & [episodeNumber]
     * via [service] for getting episodes
     */
    suspend fun getEpisodes(
        name: String,
        seasonNumber: String,
        episodeNumber: String,
    ): List<EpisodeAPI> {
        val episodeList: List<EpisodeAPI> =
            if (seasonNumber.isBlank() || episodeNumber.isBlank()) {
                service.searchEpisode(name).episodes
            } else {
                service.searchEpisode(
                    name,
                    assembleEpisodeFullNumber(seasonNumber, episodeNumber)
                ).episodes
            }
        return episodeList
    }

    override fun getName() = name
    override fun setName(name: String) {
        this.name.value = name
    }

    fun getSeasonNumber() = seasonNumber
    fun setSeasonNumber(season: String) {
        this.seasonNumber.value = season
    }

    fun getEpisodeNumber() = episodeNumber
    fun setEpisodeNumber(episode: String) {
        this.episodeNumber.value = episode
    }

    override fun getList() = episodes
    override fun setList(list: List<EpisodeAPI>) {
        this.episodes.value = list
    }

    override fun resetFilters() {
        setSeasonNumber("")
        setEpisodeNumber("")
    }

    /**
     * Retrieves the [seasonNumber] and [episodeNumber] from the user's choice and creates a string
     * for the API request formatted as SxxExx
     *
     * For example: 'S01E05' or 'S03E10'.
     */
    private fun assembleEpisodeFullNumber(
        seasonNumber: String,
        episodeNumber: String,
    ): String {
        val episodeFullNumber = StringBuilder("S")
        episodeFullNumber
            .append("0")
            .append(seasonNumber)
            .append("E")
        if (episodeNumber.length == 1) {
            episodeFullNumber.append("0")
        }
        episodeFullNumber.append(episodeNumber)
        return episodeFullNumber.toString()
    }
}