package alireza.nezami.database.di

import alireza.nezami.database.database.NoteDatabase
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
            @ApplicationContext context: Context
    ): NoteDatabase = Room.databaseBuilder(
        context, NoteDatabase::class.java, "notes_database"
    ).build()

    @Provides
    @Singleton
    fun provideNoteDao(database: NoteDatabase) = database.noteDao()
}