package alireza.nezami.database.database

import alireza.nezami.database.dao.NoteDao
import alireza.nezami.model.entity.NoteEntity
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [NoteEntity::class], version = 1, exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}