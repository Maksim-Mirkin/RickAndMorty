package edu.mlm.rick_and_morty.view_model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.mlm.rick_and_morty.repository.CharacterRepository
import edu.mlm.rick_and_morty.repository.DetailsRepository
import edu.mlm.rick_and_morty.repository.FavoriteCharacterRepository
import edu.mlm.rick_and_morty.service.dto.location.LocationAPI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import edu.mlm.rick_and_morty.compose.details_screen.CharacterDetailsScreen
import edu.mlm.rick_and_morty.compose.details_screen.FavoriteCharacterDetailsScreen

abstract class AbstractViewModel constructor(private val detailsRepo: DetailsRepository) :
    ViewModel() {

    private val _isLoading = MutableStateFlow(false)

    /**
     *  Indicates whether the service is making an API request.
     *
     *  If true, a circular progress bar is displayed.
     */
    val isLoading = _isLoading.asStateFlow()

    private val _isUnexpectedErrorShown = MutableStateFlow(false)

    /**
     * Indicates whether an unexpected exception has occurred during an API request.
     */
    val isUnexpectedErrorShown = _isUnexpectedErrorShown.asStateFlow()

    private val _isDialogShown = MutableStateFlow(false)

    /**
     * Indicates whether the filter dialog menu is being shown to the user.
     */
    val isDialogShown = _isDialogShown.asStateFlow()

    /**
     * Updates the state of [isLoading]
     */
    fun isLoading(boolean: Boolean) {
        _isLoading.value = boolean
    }

    /**
     * Updates the state of [isUnexpectedErrorShown]
     */
    fun isUnexpectedErrorShown(boolean: Boolean) {
        _isUnexpectedErrorShown.value = boolean
    }

    /**
     * Displays the filter dialog to the user.
     */
    fun showFilterDialog() {
        _isDialogShown.value = true
    }

    /**
     * Closes filter dialog for user.
     */
    fun closeFilterDialog() {
        _isDialogShown.value = false
    }

    /**
     * Retrieves the location by its [id] and navigates to the corresponding screen using
     * [navigateToLocationDetails] function received in [CharacterDetailsScreen] or
     * [FavoriteCharacterDetailsScreen].
     */
    fun getLocation(id: Int, navigateToLocationDetails: (LocationAPI) -> Unit) {
        viewModelScope.launch {
            val location = detailsRepo.getLocation(id)
            navigateToLocationDetails(location)
        }
    }

    /**
     * Provides string for searching characters by name.
     */
    @Composable
    abstract fun charactersName(): State<String>

    /**
     * Provides string for searching characters by type.
     */
    @Composable
    abstract fun charactersType(): State<String>

    /**
     * Provides string for searching characters by status.
     */
    @Composable
    abstract fun charactersStatus(): State<String>

    /**
     * Provides string for searching characters by species.
     */
    @Composable
    abstract fun charactersSpecies(): State<String>

    /**
     * Provides string for searching characters by gender.
     */
    @Composable
    abstract fun charactersGender(): State<String>

    /**
     * Hides errors from user.
     */
    abstract fun hideErrors()

    /**
     * Displays unexpected error for user.
     */
    abstract fun showUnexpectedError()

    /**
     * Update characters name filter using [name] entered by user in search bar.
     */
    abstract fun updateCharactersNameFilter(name: String)

    /**
     * Update characters status filter using [status] selected by user in filter dialog.
     */
    abstract fun updateCharactersStatusFilter(status: String)

    /**
     * Update characters species filter using [species] entered by user in filter dialog.
     */
    abstract fun updateCharactersSpeciesFilter(species: String)

    /**
     * Update characters type filter using [type] entered by user in filter dialog.
     */
    abstract fun updateCharactersTypeFilter(type: String)

    /**
     * Update characters gender filter using [gender] selected by user in filter dialog.
     */
    abstract fun updateCharacterGenderFilter(gender: String)

    /**
     * Upon updating any of the filters ([charactersName], [charactersStatus], [charactersSpecies],
     * [charactersGender], and [charactersType]), refreshes the character list.
     *
     * Clears error messages using [hideErrors], activates the circular progress bar via [isLoading],
     * and initiates an API request to the API service using [CharacterRepository] or
     * [FavoriteCharacterRepository].
     *
     * If the API throws an [Exception], displays the relevant error to the user and clears
     * the characters list.
     *
     * Finally, it deactivates the circular progress bar.
     */
    abstract fun refreshCharacters()

    /**
     * Resets characters filters in filter dialog and refresh characters list via [refreshCharacters]
     */
    abstract fun resetCharactersFilters()
}
