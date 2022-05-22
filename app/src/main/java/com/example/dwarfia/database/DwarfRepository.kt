package com.example.dwarfia.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class DwarfRepository(private val dwarfDao: DwarfDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val stars: Flow<List<Dwarf>> = dwarfDao.getStars()
    val not_visited: Flow<List<Dwarf>> = dwarfDao.getNotVisited()
    val your_collection: Flow<List<Dwarf>> = dwarfDao.getVisited()


    fun getSingleDwarf(name: String): LiveData<Dwarf> {
        var selected_dwarf = dwarfDao.getSingleDwarf(name)
        return selected_dwarf
    }

    fun markAsVisited(name: String){
        dwarfDao.markAsVisited(name)
    }

    suspend fun markAsNotVisited(name: String){
        dwarfDao.markAsNotVisited(name)
    }

    suspend fun markAsHearted(name: String){
        var curr_stars = dwarfDao.getCurrentHearts(name)
        dwarfDao.incrementHearts(curr_stars++, name)
        dwarfDao.markAsHearted(name)
    }


}