package com.example.appdevprojectradlerprueller

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverter

/**
 * buildingDAO (Data Access Object) is an interface that defines the methods to access and manipulate
 * the "buildings" table in the app's database using Room.
 */
@Dao
interface buildingDAO {

    /**
     * Retrieves all building entities from the "buildings" table in the database.
     *
     * @return A list of all building entities as a suspended List<buildingEntity>.
     */
    @Query("SELECT * FROM buildings")
    suspend fun getAll(): List<buildingEntity>

    /**
     * Inserts one or more building entities into the "buildings" table.
     *
     * @param buildings The vararg (variable-length argument) of buildingEntity objects to be inserted.
     */
    @Insert
    suspend fun insertAll(vararg buildings: buildingEntity)

    /**
     * Updates the "buildingAmount" and "buildingCost" columns of a building entity in the "buildings" table
     * based on the given buildingID.
     *
     * @param amount The new amount of buildings to be updated.
     * @param cost The new cost of the building to be updated.
     * @param id The buildingID of the building entity to be updated.
     */
    @Query("UPDATE buildings SET buildingAmount = :amount, buildingCost = :cost WHERE buildingID = :id")
    suspend fun updateById(amount: Int, cost: Int, id: Int)

    /**
     * Deletes all data from the "buildings" table, effectively clearing it.
     */
    @Query("DELETE FROM buildings")
    suspend fun nukeTable()
}
