package alireza.nezami.data.di

import alireza.nezami.domain.repository.NoteRepository
import alireza.nezami.domain.repository.NoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindNoteRepository(
            noteRepository: NoteRepositoryImpl
    ): NoteRepository
}