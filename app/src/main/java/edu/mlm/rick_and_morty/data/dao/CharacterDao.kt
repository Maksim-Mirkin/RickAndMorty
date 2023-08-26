package edu.mlm.rick_and_morty.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Upsert
import androidx.sqlite.db.SupportSQLiteQuery
import edu.mlm.rick_and_morty.data.entity.CharacterEntity

@Dao
interface CharacterDao {

    @Upsert
    suspend fun upsertCharacter(characters: CharacterEntity)

    @Query("DELETE FROM CharacterEntity WHERE id= :id")
    suspend fun deleteCharacter(id: Int)

    @Query("DELETE FROM CharacterEntity")
    suspend fun clearCharacters()

    @Query("SELECT * FROM CharacterEntity WHERE id = :id")
    suspend fun isExist(id: Int): List<CharacterEntity>

    @RawQuery
    suspend fun searchCharacters(query: SupportSQLiteQuery): List<CharacterEntity>

    @Query("SELECT * FROM CharacterEntity")
    suspend fun getCharacters(): List<CharacterEntity>
}