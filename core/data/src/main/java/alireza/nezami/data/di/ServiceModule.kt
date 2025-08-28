package alireza.nezami.data.di

import alireza.nezami.data.service.AlarmReceiver
import alireza.nezami.data.service.AlarmScheduler
import alireza.nezami.data.service.AlarmSchedulerImpl
import alireza.nezami.data.service.BootReceiver
import alireza.nezami.domain.repository.NoteRepository
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    @Singleton
    fun provideAlarmScheduler(
        @ApplicationContext context: Context
    ): AlarmScheduler {
        return AlarmSchedulerImpl(context)
    }

//    @Provides
//    @Singleton
//    fun provideAlarmReceiver(
//        noteRepository: NoteRepository,
//        alarmScheduler: AlarmScheduler
//    ): AlarmReceiver {
//        return AlarmReceiver(noteRepository, alarmScheduler)
//    }

    @Provides
    @Singleton
    fun provideBootReceiver(
        noteRepository: NoteRepository,
        alarmScheduler: AlarmScheduler
    ): BootReceiver {
        return BootReceiver(noteRepository, alarmScheduler)
    }
}