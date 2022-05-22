package com.example.dwarfia.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DwarfDao {

    @Query("SELECT * FROM dwarfs_table ORDER BY stars_count DESC")
    fun getStars(): Flow<List<Dwarf>>

    @Query("SELECT * FROM dwarfs_table WHERE visited = 1")
    fun getNotVisited(): Flow<List<Dwarf>>

    @Query("SELECT * FROM dwarfs_table WHERE visited = 0")
    fun getVisited(): Flow<List<Dwarf>>

    @Query("SELECT * FROM dwarfs_table WHERE name = :name")
    fun getSingleDwarf(name: String): LiveData<Dwarf>

    @Query("UPDATE dwarfs_table SET visited=0 WHERE name = :name")
    fun markAsVisited(name: String)

    @Query("UPDATE dwarfs_table SET visited=1 WHERE name = :name")
    suspend fun markAsNotVisited(name: String)

    @Query("SELECT hearts_count FROM dwarfs_table WHERE name = :name")
    fun getCurrentHearts(name: String): Int

    @Query("UPDATE dwarfs_table SET hearts_count=:hearts_count WHERE name = :name")
    fun incrementHearts(hearts_count: Int, name: String): Void

    @Query("UPDATE dwarfs_table SET hearted=0 WHERE name = :name")
    suspend fun markAsHearted(name: String): Void


}