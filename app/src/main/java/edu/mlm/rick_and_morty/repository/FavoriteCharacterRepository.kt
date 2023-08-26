package edu.mlm.rick_and_morty.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import edu.mlm.rick_and_morty.data.dao.CharacterDao
import edu.mlm.rick_and_morty.data.dao.EpisodeDao
import edu.mlm.rick_and_morty.data.entity.CharacterEntity
import edu.mlm.rick_and_morty.data.entity.EpisodeEntity
import edu.mlm.rick_and_morty.data.mappers.toEntity
import edu.mlm.rick_and_morty.service.RAMService
import edu.mlm.rick_and_morty.service.dto.character.CharacterAPI
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import edu.mlm.rick_and_morty.compose.regulars_screens.FavoriteCharacterScreen
import edu.mlm.rick_and_morty.compose.details_screen.FavoriteCharacterDetailsScreen

class FavoriteCharacterRepository @Inject constructor(
    private val characterDao: CharacterDao,
    private val episodeDao: EpisodeDao,
    private val service: RAMService,
) : Repository<CharacterEntity>(service) {

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
     * A list of [CharacterEntity] for user display on [FavoriteCharacterScreen]
     */
    private var characters = MutableStateFlow(listOf<CharacterEntity>())

    /**
     * A list of [EpisodeEntity] for user display on [FavoriteCharacterDetailsScreen]
     */
    private var episodes = MutableStateFlow(listOf<EpisodeEntity>())

    override fun getName() = name
    override fun setName(name: String) {
        this.name.value = name
    }

    override fun getList() = characters
    override fun setList(list: List<CharacterEntity>) {
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

    fun getEpisodes() = episodes
    fun setEpisodes(episodes: List<EpisodeEntity>) {
        this.episodes.value = episodes
    }

    override fun resetFilters() {
        type.value = ""
        gender.value = ""
        species.value = ""
        status.value = ""
    }

    /**
     * Converts [characterAPI] to [CharacterEntity] and performs an UPSERT operation into
     * the Room Database.
     *
     * Additionally, extracts episodes from the characters and adds them to the database.
     */
    suspend fun addCharacterToFavorite(characterAPI: CharacterAPI) {
        characterDao.upsertCharacter(characterAPI.toEntity())
        val episodeIds = characterDao.isExist(characterAPI.id)[0].episodeIds
        for (id in episodeIds) {
            episodeDao.addEpisode(service.getEpisode(id).toEntity())
        }
    }

    /**
     * Performs an UPSERT operation into the Room Database for the [character] for display in the
     * [FavoriteCharacterScreen].
     */
    suspend fun addCharacterToFavorite(character: CharacterEntity) {
        characterDao.upsertCharacter(character)
    }

    /**
     * Performs a DELETE operation for [CharacterEntity] from Room Database using its [id]
     */
    suspend fun removeCharacterFromFavorite(id: Int) {
        characterDao.deleteCharacter(id)
    }

    /**
     * Performs a SELECT operation in the Room Database using the provided [id] and checks
     * if a character with the same ID exists.
     */
    suspend fun isExist(id: Int): Boolean {
        return characterDao.isExist(id).isNotEmpty()
    }

    /**
     * Performs a DELETE operation for entire [CharacterEntity] table and [EpisodeEntity] table.
     *
     */
    suspend fun clearCharacters() {
        characterDao.clearCharacters()
        episodeDao.clearEpisodes()
    }

    /**
     * Creates a database query using the retrieved strings: [name], [status], [species], [gender]
     * & [type].
     *
     * Then, performs a SELECT operation from the database based on this query.
     */
    suspend fun getCharacters(
        name: String,
        status: String,
        species: String,
        gender: String,
        type: String,
    ): List<CharacterEntity> {
        val baseQuery = StringBuilder("SELECT * FROM CharacterEntity ")
        val nameQuery = "WHERE name LIKE '%' || :name || '%' "
        val statusQuery = "status LIKE :status || '%' "
        val speciesQuery = "species LIKE '%' || :species || '%' "
        val genderQuery = "gender LIKE :gender || '%' "
        val typeQuery = "type LIKE '%' || :type || '%' "
        val and = "AND "
        val where = "WHERE "
        val bindArgs = arrayListOf<String>()
        var containsCondition = false
        if (name.isNotBlank()) {
            baseQuery.append(nameQuery)
            bindArgs.add(name)
            containsCondition = true
        }
        if (status.isNotBlank()) {
            if (containsCondition) {
                baseQuery.append(and)
            } else {
                baseQuery.append(where)
            }
            baseQuery.append(statusQuery)
            bindArgs.add(status)
            containsCondition = true
        }
        if (species.isNotBlank()) {
            if (containsCondition) {
                baseQuery.append(and)
            } else {
                baseQuery.append(where)
            }
            baseQuery.append(speciesQuery)
            bindArgs.add(species)
            containsCondition = true
        }
        if (gender.isNotBlank()) {
            if (containsCondition) {
                baseQuery.append(and)
            } else {
                baseQuery.append(where)
            }
            baseQuery.append(genderQuery)
            bindArgs.add(gender)
            containsCondition = true
        }
        if (type.isNotBlank()) {
            if (containsCondition) {
                baseQuery.append(and)
            } else {
                baseQuery.append(where)
            }
            baseQuery.append(typeQuery)
            bindArgs.add(type)
        }
        val query = SimpleSQLiteQuery(
            query = baseQuery.toString(),
            bindArgs = bindArgs.toArray()
        )

        return characterDao.searchCharacters(query)
    }

    /**
     * Performs a SELECT operation on the database using the retrieved list of [episodeIds].
     */
    suspend fun getEpisodes(episodeIds: List<Int>) = episodeDao.getEpisodes(episodeIds = episodeIds)

    /**
     * Performs a SELECT operation on the database and checks if its empty.
     */
    suspend fun isDatabaseEmpty() = characterDao.getCharacters().isEmpty()
}
