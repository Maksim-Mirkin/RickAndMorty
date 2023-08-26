package edu.mlm.rick_and_morty.service

import edu.mlm.rick_and_morty.service.dto.character.CharacterAPI
import edu.mlm.rick_and_morty.service.dto.character.CharacterResponse
import edu.mlm.rick_and_morty.service.dto.episode.EpisodeAPI
import edu.mlm.rick_and_morty.service.dto.episode.EpisodeResponse
import edu.mlm.rick_and_morty.service.dto.location.LocationAPI
import edu.mlm.rick_and_morty.service.dto.location.LocationResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RAMService {

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): CharacterAPI

    @GET("episode/{id}")
    suspend fun getEpisode(@Path("id") id: Int): EpisodeAPI

    @GET("location/{id}")
    suspend fun getLocation(@Path("id") id: Int): LocationAPI

    @GET("character")
    suspend fun searchCharacter(
        @Query("name") name: String,
        @Query("status") status: String,
        @Query("species") species: String,
        @Query("gender") gender: String,
        @Query("type") type: String,
    ): CharacterResponse

    @GET("episode")
    suspend fun searchEpisode(
        @Query("name") name: String,
        @Query("episode") episode: String = "",
    ): EpisodeResponse

    @GET("location")
    suspend fun searchLocation(
        @Query("name") name: String,
        @Query("type") type: String,
        @Query("dimension") dimension: String,
    ): LocationResponse
}