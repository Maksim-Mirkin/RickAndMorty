package edu.mlm.rick_and_morty.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.mlm.rick_and_morty.data.converters.RAMConverters
import edu.mlm.rick_and_morty.data.dao.CharacterDao
import edu.mlm.rick_and_morty.data.dao.EpisodeDao
import edu.mlm.rick_and_morty.data.entity.CharacterEntity
import edu.mlm.rick_and_morty.data.entity.EpisodeEntity


const val DB_VERSION = 1
const val DB_NAME = "RAMAppDatabase"

@TypeConverters(RAMConverters::class)
@Database(
    entities = [CharacterEntity::class, EpisodeEntity::class],
    version = DB_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun episodeDao(): EpisodeDao
}