package edu.mlm.rick_and_morty.repository

import edu.mlm.rick_and_morty.service.RAMService
import kotlinx.coroutines.flow.MutableStateFlow

abstract class Repository<T>(service: RAMService) {

    abstract fun getList(): MutableStateFlow<List<T>>
    abstract fun setList(list: List<T>)

    abstract fun getName(): MutableStateFlow<String>
    abstract fun setName(name: String)


    /**
     * Sets all string states to empty.
     */
    abstract fun resetFilters()
}
