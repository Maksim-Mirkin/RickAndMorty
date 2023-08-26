package edu.mlm.rick_and_morty.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import edu.mlm.rick_and_morty.data.entity.EpisodeEntity

@Dao
interface EpisodeDao {

    @Upsert
    suspend fun addEpisode(episode: EpisodeEntity)

    @Query("SELECT * FROM EpisodeEntity WHERE id IN (:episodeIds)")
    suspend fun getEpisodes(episodeIds: List<Int>): List<EpisodeEntity>

    @Query("DELETE FROM EpisodeEntity")
    suspend fun clearEpisodes()
}