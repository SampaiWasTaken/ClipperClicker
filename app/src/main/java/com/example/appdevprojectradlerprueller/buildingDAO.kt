package com.example.appdevprojectradlerprueller

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface buildingDAO {
    @Query("SELECT * FROM buildings")
    suspend fun getAll(): List<buildingEntity>

    @Insert
    suspend fun insertAll(vararg buildings: buildingEntity)

    @Query("UPDATE buildings SET buildingAmount = :amount, buildingCost = :cost WHERE buildingID = :id")
    suspend fun updateById(amount: Int, cost: Int, id: Int)

    @Query("SELECT (SELECT COUNT(*) FROM buildings) == 0")
    fun isEmpty(): Boolean
}