package edu.mlm.rick_and_morty.view_model

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.mlm.rick_and_morty.repository.FavoriteCharacterRepository
import edu.mlm.rick_and_morty.repository.CharacterRepository
import edu.mlm.rick_and_morty.repository.DetailsRepository
import edu.mlm.rick_and_morty.repository.EpisodeRepository
import edu.mlm.rick_and_morty.repository.LocationRepository
import edu.mlm.rick_and_morty.service.dto.character.CharacterAPI
import edu.mlm.rick_and_morty.service.dto.episode.EpisodeAPI
import edu.mlm.rick_and_morty.service.dto.location.LocationAPI
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
import edu.mlm.rick_and_morty.compose.regulars_screens.*
import edu.mlm.rick_and_morty.compose.details_screen.CharacterDetailsScreen
import edu.mlm.rick_and_morty.compose.details_screen.EpisodeDetailsScreen
import edu.mlm.rick_and_morty.compose.details_screen.LocationDetailsScreen

private const val TAG = "MainViewModel"

@OptIn(FlowPreview::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val characterRepo: CharacterRepository,
    private val episodeRepo: EpisodeRepository,
    private val locationRepo: LocationRepository,
    private val detailsRepo: DetailsRepository,
    private val favoriteCharacterRepo: FavoriteCharacterRepository,
) : AbstractViewModel(detailsRepo) {

    private val _isHttpErrorShown = MutableStateFlow(false)

    /**
     * Indicates if HTTP exception occurs during API request.
     */
    val isHttpErrorShown = _isHttpErrorShown.asStateFlow()

    /**
     * Composables and property for [CharacterScreen].
     */
    /**
     *  Provides list of [CharacterAPI] for display to the user.
     */
    val characters = characterRepo.getName()
        .debounce(500L)
        .combine(characterRepo.getList()) { _, characters ->
            refreshCharacters()
            characters
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            characterRepo.getList().value
        )

    @Composable
    override fun charactersName() = characterRepo.getName().collectAsState()

    @Composable
    override fun charactersType() = characterRepo.getType().collectAsState()

    @Composable
    override fun charactersStatus() = characterRepo.getStatus().collectAsState()

    @Composable
    override fun charactersSpecies() = characterRepo.getSpecies().collectAsState()

    @Composable
    override fun charactersGender() = characterRepo.getGender().collectAsState()

    /**
     * Composables and property for [EpisodeScreen].
     */
    /**
     * Provides list of [EpisodeAPI] for display to the user.
     */
    val episodes = episodeRepo.getName()
        .debounce(500L)
        .combine(episodeRepo.getList()) { _, episodes ->
            refreshEpisodes()
            episodes
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            episodeRepo.getList().value
        )

    /**
     * Provides string for searching episodes by name.
     */
    @Composable
    fun episodesName() = episodeRepo.getName().collectAsState()

    /**
     * Provides string for searching episodes by seasons number.
     */
    @Composable
    fun seasonNumber() = episodeRepo.getSeasonNumber().collectAsState()

    /**
     * Provides string for searching episodes by episodes number.
     */
    @Composable
    fun episodeNumber() = episodeRepo.getEpisodeNumber().collectAsState()

    /**
     * Composables and property for [LocationScreen].
     */
    /**
     *  Provides list of [LocationAPI] for display to the user.
     */
    val locations = locationRepo.getName()
        .debounce(500L)
        .combine(locationRepo.getList()) { _, locations ->
            refreshLocations()
            locations
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            locationRepo.getList().value
        )

    /**
     * Provides string for searching locations by name.
     */
    @Composable
    fun locationName() = locationRepo.getName().collectAsState()

    /**
     * Provides string for searching locations by type.
     */
    @Composable
    fun locationType() = locationRepo.getType().collectAsState()

    /**
     * Provides string for searching locations by dimension.
     */
    @Composable
    fun locationDimension() = locationRepo.getDimension().collectAsState()


    /**
     * Provides list of [EpisodeAPI] for display on [CharacterDetailsScreen]
     */
    @Composable
    fun episodes() = detailsRepo.getEpisodes().collectAsState()

    /**
     * Provides list of [CharacterAPI] for display on [EpisodeDetailsScreen] & [LocationDetailsScreen]
     */
    @Composable
    fun characters() = detailsRepo.getCharacters().collectAsState()


    /**
     * Adds a character and its relative episodes to the Room Database using [favoriteCharacterRepo]
     * and saves it for the user in [FavoriteCharacterScreen].
     */
    fun addCharacterToFavorite(character: CharacterAPI) {
        viewModelScope.launch {
            favoriteCharacterRepo.addCharacterToFavorite(character)
        }
    }

    /**
     * Removes a character from the Room Database using [favoriteCharacterRepo] within [FavoriteCharacterScreen].
     */
    fun removeCharacterFromFavorite(character: CharacterAPI) {
        viewModelScope.launch {
            favoriteCharacterRepo.removeCharacterFromFavorite(character.id)
        }
    }


    override fun updateCharactersNameFilter(name: String) {
        characterRepo.setName(name)
    }


    override fun updateCharactersSpeciesFilter(species: String) {
        characterRepo.setSpecies(species)
    }


    override fun updateCharactersTypeFilter(type: String) {
        characterRepo.setType(type)
    }


    override fun updateCharactersStatusFilter(status: String) {
        characterRepo.setStatus(status)
    }


    override fun updateCharacterGenderFilter(gender: String) {
        characterRepo.setGender(gender)
    }


    override fun resetCharactersFilters() {
        characterRepo.resetFilters()
        refreshCharacters()
    }

    override fun refreshCharacters() {
        hideErrors()
        viewModelScope.launch {
            try {
                isLoading(true)
                characterRepo.setList(
                    characterRepo.getCharacters(
                        characterRepo.getName().value,
                        characterRepo.getStatus().value,
                        characterRepo.getSpecies().value,
                        characterRepo.getGender().value,
                        characterRepo.getType().value
                    )
                )
            } catch (e: HttpException) {
                characterRepo.setList(listOf())
                showHttpError()
                Log.d(TAG, "refreshCharacters: ${e.stackTraceToString()}")
            } catch (e: Exception) {
                characterRepo.setList(listOf())
                showUnexpectedError()
                Log.d(TAG, "refreshCharacters: ${e.stackTraceToString()}")
            } finally {
                isLoading(false)
            }
        }
    }

    /**
     * Update episodes name filter using [name] entered by user in search bar.
     */
    //episodes
    fun updateEpisodeNameFilter(name: String) {
        episodeRepo.setName(name)
    }

    /**
     * Update seasons number filter using [season] selected by user in filter dialog.
     */
    fun updateSeasonsNumberFilter(season: String) {
        episodeRepo.setSeasonNumber(season)
    }

    /**
     * Update episodes number filter using [episode] selected by user in filter dialog.
     */
    fun updateEpisodesNumberFilter(episode: String) {
        episodeRepo.setEpisodeNumber(episode)
    }

    /**
     * Resets episodes filters in filter dialog and refresh episodes list via [refreshEpisodes]
     */
    fun resetEpisodesFilters() {
        episodeRepo.resetFilters()
        refreshEpisodes()
    }

    /**
     * Upon updating any of the filters ([episodesName], [seasonNumber] & [episodeNumber]),
     * refreshes the episode list.
     *
     * Clears error messages using [hideErrors], activates the circular progress bar via [isLoading],
     * and initiates an API request to the API service using [episodeRepo].
     *
     * If the API throws an [Exception], displays the relevant error to the user and clears
     * the episode list.
     *
     * Finally, it deactivates the circular progress bar.
     */
    fun refreshEpisodes() {
        hideErrors()
        viewModelScope.launch {
            try {
                isLoading(true)
                episodeRepo.setList(
                    episodeRepo.getEpisodes(
                        episodeRepo.getName().value,
                        episodeRepo.getSeasonNumber().value,
                        episodeRepo.getEpisodeNumber().value
                    )
                )
            } catch (e: HttpException) {
                episodeRepo.setList(listOf())
                showHttpError()
                Log.d(TAG, "getFilteredEpisodeList: ${e.stackTraceToString()} ")
            } catch (e: Exception) {
                episodeRepo.setList(listOf())
                showUnexpectedError()
                Log.d(TAG, "getEpisodeList: ${e.stackTraceToString()}")
            } finally {
                isLoading(false)
            }
        }
    }

    /**
     * Update locations name filter using [name] entered by user in search bar
     */
    fun updateLocationsNameFilter(name: String) {
        locationRepo.setName(name)
    }

    /**
     * Update locations type filter using [type] entered by user in dialog filter
     */
    fun updateLocationsTypeFilter(type: String) {
        locationRepo.setType(type)
    }

    /**
     * Update dimensions filter using [dimension] entered by user in dialog filter.
     */
    fun updateDimensionsFilter(dimension: String) {
        locationRepo.setDimension(dimension)
    }

    /**
     * Resets locations filters in filter dialog and refresh locations list via [refreshLocations]
     */
    fun resetLocationsFilters() {
        locationRepo.resetFilters()
        refreshLocations()
    }

    /**
     * Upon updating any of the filters ([locationName], [locationType] & [locationDimension]),
     * refreshes the location list.
     *
     * Clears error messages using [hideErrors], activates the circular progress bar via [isLoading],
     * and initiates an API request to the API service using [locationRepo].
     *
     * If the API throws an [Exception], displays the relevant error to the user and clears
     * the location list.
     *
     * Finally, it deactivates the circular progress bar.
     */
    fun refreshLocations() {
        hideErrors()
        viewModelScope.launch {
            viewModelScope.launch {
                try {
                    isLoading(true)
                    locationRepo.setList(
                        locationRepo.getLocations(
                            locationRepo.getName().value,
                            locationRepo.getType().value,
                            locationRepo.getDimension().value,
                        )
                    )

                } catch (e: HttpException) {
                    locationRepo.setList(listOf())
                    showHttpError()
                    Log.d(TAG, "getFilteredLocationList: ${e.stackTraceToString()}")
                } catch (e: Exception) {
                    showUnexpectedError()
                    Log.d(TAG, "getFilteredLocationList: ${e.stackTraceToString()}")
                } finally {
                    isLoading(false)
                }
            }
        }
    }


    /**
     * Retrieve episodes from [CharacterDetailsScreen].
     *
     * Clear and remove error messages using [hideErrors], then activate the circular progress bar
     * via [isLoading] and initiate an API request to the service through [detailsRepo].
     *
     * If the API throws an Exception, display an error message to the user.
     *
     * Finally, deactivate the circular progress bar.
     */
    fun getEpisodeList(episodes: List<String>) {
        hideErrors()
        viewModelScope.launch {
            try {
                isLoading(true)
                detailsRepo.setEpisodes(listOf())
                detailsRepo.setEpisodes(detailsRepo.getEpisodeList(episodes))
            } catch (e: Exception) {
                showUnexpectedError()
                Log.d(TAG, "getEpisodeList: ${e.stackTraceToString()} ")
            } finally {
                isLoading(false)
            }
        }
    }

    /**
     * Retrieve characters from [EpisodeDetailsScreen] or [LocationDetailsScreen]
     *
     * Clear and remove error messages using [hideErrors], then activate the circular progress bar
     * via [isLoading] and initiate an API request to the service through [detailsRepo].
     *
     * If the API throws an Exception, display an error message to the user.
     *
     * Finally, deactivate the circular progress bar.
     */
    fun getCharacterList(characters: List<String>) {
        hideErrors()
        viewModelScope.launch {
            try {
                isLoading(true)
                detailsRepo.setCharacters(listOf())
                detailsRepo.setCharacters(detailsRepo.getCharacterList(characters))
            } catch (e: Exception) {
                showUnexpectedError()
                Log.d(TAG, "getCharacterList: ${e.stackTrace}")
            } finally {
                isLoading(false)
            }
        }
    }


    override fun showUnexpectedError() {
        isUnexpectedErrorShown(true)
    }

    /**
     * Displays an HTTP error to the user.
     */
    private fun showHttpError() {
        _isHttpErrorShown.value = true
    }


    override fun hideErrors() {
        isUnexpectedErrorShown(false)
        _isHttpErrorShown.value = false
    }

    /**
     * Checks if a character exists in the database through [favoriteCharacterRepo] to mark them as
     * a favorite.
     */
    suspend fun isExist(id: Int) = favoriteCharacterRepo.isExist(id)
}