package com.example.dwarfia

import android.app.Application
import com.example.dwarfia.database.DwarfDatabase
import com.example.dwarfia.database.DwarfRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class DwarfsApplication: Application() {
    val appScope = CoroutineScope(SupervisorJob())
    val database by lazy { DwarfDatabase.getDatabase(this, appScope) }
    val repository by lazy { DwarfRepository(database.dwarfDao()) }
}