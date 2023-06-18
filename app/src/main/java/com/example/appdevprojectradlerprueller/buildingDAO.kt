package com.example.appdevprojectradlerprueller

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverter

@Dao
interface buildingDAO {
    @Query("SELECT * FROM buildings")
    suspend fun getAll(): List<buildingEntity>

    @Insert
    suspend fun insertAll(vararg buildings: buildingEntity)

    @Query("UPDATE buildings SET buildingAmount = :amount, buildingCost = :cost WHERE buildingID = :id")
    suspend fun updateById(amount: Int, cost: Int, id: Int)

    @Query("DELETE FROM buildings")
    suspend fun nukeTable()
}