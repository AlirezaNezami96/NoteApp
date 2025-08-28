package alireza.nezami.database.di

import alireza.nezami.database.dao.NoteDao
import alireza.nezami.database.database.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DaosModule {
    @Provides
    fun provideNoteDao(
            database: NoteDatabase,
    ): NoteDao = database.noteDao()

}
