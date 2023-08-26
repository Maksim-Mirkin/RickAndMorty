package edu.mlm.rick_and_morty.view_model

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.mlm.rick_and_morty.data.entity.CharacterEntity
import edu.mlm.rick_and_morty.repository.FavoriteCharacterRepository
import edu.mlm.rick_and_morty.repository.DetailsRepository
import edu.mlm.rick_and_morty.service.dto.episode.EpisodeAPI
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import edu.mlm.rick_and_morty.data.entity.EpisodeEntity
import edu.mlm.rick_and_morty.compose.regulars_screens.FavoriteCharacterScreen
import edu.mlm.rick_and_morty.compose.details_screen.FavoriteCharacterDetailsScreen

private const val TAG = "FavoriteCharacterViewModel"

@OptIn(FlowPreview::class)
@HiltViewModel
class FavoriteCharacterViewModel @Inject constructor(
    private val favoriteCharacterRepo: FavoriteCharacterRepository,
    private val detailsRepo: DetailsRepository,
) :
    AbstractViewModel(detailsRepo) {

    private val _isEmptyDatabaseError = MutableStateFlow(false)

    /**
     * Indicates if database is empty.
     */
    val isEmptyDatabaseError = _isEmptyDatabaseError.asStateFlow()

    private val _isNoSuchCharactersInDb = MutableStateFlow(false)

    /**
     * Indicates if the search parameter doesn't match any character in the database.
     */
    val isNoSuchCharactersInDb = _isNoSuchCharactersInDb

    @Composable
    override fun charactersName() = favoriteCharacterRepo.getName().collectAsState()

    @Composable
    override fun charactersStatus() = favoriteCharacterRepo.getStatus().collectAsState()

    @Composable
    override fun charactersSpecies() = favoriteCharacterRepo.getSpecies().collectAsState()

    @Composable
    override fun charactersGender() = favoriteCharacterRepo.getGender().collectAsState()

    @Composable
    override fun charactersType() = favoriteCharacterRepo.getType().collectAsState()

    /**
     * Provides list of [EpisodeEntity] for display to the user.
     */
    @Composable
    fun episodes() = favoriteCharacterRepo.getEpisodes().collectAsState()

    /**
     * Provides list of [CharacterEntity] for display to the user.
     */
    val characters = favoriteCharacterRepo.getName()
        .debounce(500L)
        .combine(favoriteCharacterRepo.getList()) { _, characters ->
            refreshCharacters()
            characters
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            favoriteCharacterRepo.getList().value
        )

    /**
     * Adds a character to the Room Database using [favoriteCharacterRepo]and saves it for the user
     * in [FavoriteCharacterScreen].
     */
    fun addCharacterToFavorite(character: CharacterEntity) {
        viewModelScope.launch {
            favoriteCharacterRepo.addCharacterToFavorite(character)
        }
    }

    /**
     * Removes a character from the Room Database using [favoriteCharacterRepo] within
     * [FavoriteCharacterScreen].
     */
    fun removeCharacterFromFavorite(character: CharacterEntity) {
        viewModelScope.launch {
            favoriteCharacterRepo.removeCharacterFromFavorite(character.id)
            refreshCharacters()
        }
    }

    override fun updateCharactersNameFilter(name: String) {
        favoriteCharacterRepo.setName(name)
    }

    override fun updateCharactersSpeciesFilter(species: String) {
        favoriteCharacterRepo.setSpecies(species)
    }

    override fun updateCharactersTypeFilter(type: String) {
        favoriteCharacterRepo.setType(type)
    }

    override fun updateCharactersStatusFilter(status: String) {
        favoriteCharacterRepo.setStatus(status)
    }

    override fun updateCharacterGenderFilter(gender: String) {
        favoriteCharacterRepo.setGender(gender)
    }

    override fun resetCharactersFilters() {
        favoriteCharacterRepo.resetFilters()
        refreshCharacters()
    }

    override fun refreshCharacters() {
        hideErrors()
        viewModelScope.launch {
            try {
                isLoading(true)
                if (!favoriteCharacterRepo.isDatabaseEmpty()) {
                    favoriteCharacterRepo.setList(
                        favoriteCharacterRepo.getCharacters(
                            favoriteCharacterRepo.getName().value,
                            favoriteCharacterRepo.getStatus().value,
                            favoriteCharacterRepo.getSpecies().value,
                            favoriteCharacterRepo.getGender().value,
                            favoriteCharacterRepo.getType().value
                        )
                    )
                    if (favoriteCharacterRepo.getList().value.isEmpty()) {
                        showNoSuchCharactersInDbError()
                    }
                } else {
                    showEmptyDatabaseError()
                }
            } catch (e: Exception) {
                showUnexpectedError()
                Log.d(
                    TAG, "refreshView: ${e.stackTraceToString()}"
                )
            } finally {
                isLoading(false)
            }
        }
    }

    /**
     * Clears characters and episodes from the database using [favoriteCharacterRepo], and then
     * resets the searching filters in [FavoriteCharacterScreen] using [resetCharactersFilters] and
     * [updateCharactersNameFilter].
     */
    fun clearCharacters() {
        viewModelScope.launch {
            favoriteCharacterRepo.clearCharacters()
            resetCharactersFilters()
            updateCharactersNameFilter("")
        }
    }

    /**
     * Retrieves a list of [episodeIds] and provides it to [favoriteCharacterRepo] to set episodes
     * for [FavoriteCharacterDetailsScreen].
     */
    fun getEpisodes(episodeIds: List<Int>) {
        viewModelScope.launch {
            favoriteCharacterRepo.setEpisodes(favoriteCharacterRepo.getEpisodes(episodeIds))
        }
    }

    /**
     * Retrieves the episode by its [id] and navigates to the corresponding screen using
     * [navigateToEpisodeDetails] function received in
     * [edu.mlm.rick_and_morty.compose.details_screen.FavoriteCharacterDetailsScreen].
     */
    fun getEpisode(id: Int, navigateToEpisodeDetails: (EpisodeAPI) -> Unit) {
        viewModelScope.launch {
            val episode = detailsRepo.getEpisode(id)
            navigateToEpisodeDetails(episode)
        }
    }

    override fun showUnexpectedError() {
        isUnexpectedErrorShown(true)
        favoriteCharacterRepo.setList(listOf())
    }

    /**
     * Displays to the user that database is empty.
     */
    private fun showEmptyDatabaseError() {
        _isEmptyDatabaseError.value = true
        favoriteCharacterRepo.setList(listOf())
    }

    /**
     * Displays a message to the user that the search didn't match any characters in the database.
     */
    private fun showNoSuchCharactersInDbError() {
        _isNoSuchCharactersInDb.value = true
    }

    override fun hideErrors() {
        isUnexpectedErrorShown(false)
        _isEmptyDatabaseError.value = false
        _isNoSuchCharactersInDb.value = false
    }
}