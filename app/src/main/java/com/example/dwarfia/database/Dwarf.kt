package com.example.dwarfia.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "dwarfs_table")
data class Dwarf
    (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "dwarf_id") val dwarf_id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "stars_count") val stars_count: Int,
    @ColumnInfo(name = "hearts_count") val hearts_count: Int,
    @ColumnInfo(name = "thumbs_up_count") val thumbs_up_count: Int,
    @ColumnInfo(name = "visited") val visited: Int, // 0 - yes, 1 - no
    @ColumnInfo(name = "starred") val starred: Int, // 0 - yes, 1 - no
    @ColumnInfo(name = "hearted") val hearted: Int, // 0 - yes, 1 - no
    @ColumnInfo(name = "thumbed_up") val thumbed_up: Int // 0 - yes, 1 - no
    ): Parcelable