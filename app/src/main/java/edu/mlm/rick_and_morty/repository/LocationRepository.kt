package edu.mlm.rick_and_morty.repository

import edu.mlm.rick_and_morty.service.RAMService
import edu.mlm.rick_and_morty.service.dto.location.LocationAPI
import kotlinx.coroutines.flow.MutableStateFlow

import javax.inject.Inject

class LocationRepository @Inject constructor(private val service: RAMService) :
    Repository<LocationAPI>(service) {

    /**
     * Container for locations name filter.
     */
    private val name = MutableStateFlow("")

    /**
     * Container for locations type filter.
     */
    private val type = MutableStateFlow("")

    /**
     * Container for locations dimension filter.
     */
    private val dimension = MutableStateFlow("")

    /**
     * A list of [LocationAPI] for user display on various screens.
     */
    private val locations = MutableStateFlow(listOf<LocationAPI>())

    override fun getName() = name
    override fun setName(name: String) {
        this.name.value = name
    }

    override fun getList() = locations
    override fun setList(list: List<LocationAPI>) {
        this.locations.value = list
    }

    fun getType() = type
    fun setType(type: String) {
        this.type.value = type
    }

    fun getDimension() = dimension
    fun setDimension(dimension: String) {
        this.dimension.value = dimension
    }

    override fun resetFilters() {
        type.value = ""
        dimension.value = ""
    }

    /**
     * Makes API request using [name], [type] & [dimension] via [service] for getting episodes
     */
    suspend fun getLocations(name: String, type: String, dimension: String) =
        service.searchLocation(name, type, dimension).locations
}