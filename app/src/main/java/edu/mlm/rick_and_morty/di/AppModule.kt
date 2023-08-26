package edu.mlm.rick_and_morty.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.mlm.rick_and_morty.data.AppDatabase
import edu.mlm.rick_and_morty.data.DB_NAME
import edu.mlm.rick_and_morty.service.RAMService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://rickandmortyapi.com/api/"

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRAMService(): RAMService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RAMService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext
        context: Context,
    ) =
        Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
            .build()

    @Provides
    fun provideCharacterDao(db: AppDatabase) = db.characterDao()

    @Provides
    fun provideEpisodeDao(db: AppDatabase) = db.episodeDao()
}
