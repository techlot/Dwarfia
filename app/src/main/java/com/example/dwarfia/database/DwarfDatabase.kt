package com.example.dwarfia.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(Dwarf::class), version = 1, exportSchema = false)
abstract class DwarfDatabase : RoomDatabase() {

    abstract fun dwarfDao(): DwarfDao

    private class DwarfDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        //override fun onCreate(db: SupportSQLiteDatabase) {
        //    super.onCreate(db)
        //    INSTANCE?.let { database ->
        //        scope.launch {
        //        }
        //    }
        //}
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: DwarfDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): DwarfDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DwarfDatabase::class.java,
                    "dwarf_database"
                ).createFromAsset("database/dwarfs.db").allowMainThreadQueries().build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}