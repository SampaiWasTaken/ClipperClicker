package com.example.appdevprojectradlerprueller

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [buildingEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase(){
    abstract fun buildingDao(): buildingDAO
}