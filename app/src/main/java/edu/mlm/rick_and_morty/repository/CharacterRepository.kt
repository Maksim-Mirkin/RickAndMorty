package edu.mlm.rick_and_morty.repository

import edu.mlm.rick_and_morty.service.RAMService
import edu.mlm.rick_and_morty.service.dto.character.CharacterAPI
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class CharacterRepository @Inject constructor(private val service: RAMService) :
    Repository<CharacterAPI>(service) {

    /**
     * Container for characters name filter.
     */
    private var name = MutableStateFlow("")

    /**
     * Container for characters species filter.
     */
    private var species = MutableStateFlow("")

    /**
     * Container for characters type filter.
     */
    private var type = MutableStateFlow("")

    /**
     * Container for characters status filter.
     */
    private var status = MutableStateFlow("")

    /**
     * Container for characters gender filter.
     */
    private var gender = MutableStateFlow("")

    /**
     * A list of [CharacterAPI] for user display on various screens.
     */
    private var characters = MutableStateFlow(listOf<CharacterAPI>())

    override fun getName() = name
    override fun setName(name: String) {
        this.name.value = name
    }

    override fun getList() = characters
    override fun setList(list: List<CharacterAPI>) {
        this.characters.value = list
    }

    fun getSpecies() = species
    fun setSpecies(species: String) {
        this.species.value = species
    }

    fun getType() = type
    fun setType(type: String) {
        this.type.value = type
    }

    fun getStatus() = status
    fun setStatus(status: String) {
        this.status.value = status
    }

    fun getGender() = gender
    fun setGender(gender: String) {
        this.gender.value = gender
    }

    override fun resetFilters() {
        type.value = ""
        gender.value = ""
        species.value = ""
        status.value = ""
    }

    /**
     * Makes API request using [name], [status], [species], [gender] & [type]
     * via [service] for getting characters
     */
    suspend fun getCharacters(
        name: String,
        status: String,
        species: String,
        gender: String,
        type: String,
    ) = service.searchCharacter(name, status, species, gender, type).characters
}