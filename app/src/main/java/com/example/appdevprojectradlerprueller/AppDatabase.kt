package com.example.appdevprojectradlerprueller

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * AppDatabase is a Room database class that defines the database configuration for the app.
 * It is an abstract class that extends RoomDatabase and represents the main entry point
 * for interacting with the database and its associated DAOs.
 */
@Database(entities = [buildingEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase(){
    /**
     * Returns the DAO (Data Access Object) for interacting with the "buildings" table in the database.
     *
     * @return The buildingDAO that provides methods for CRUD (Create, Read, Update, Delete) operations
     *         on the "buildings" table.
     */
    abstract fun buildingDao(): buildingDAO
}