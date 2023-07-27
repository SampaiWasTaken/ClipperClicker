package com.example.appdevprojectradlerprueller
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigInteger

/**
 * buildingEntity is a data class representing a building entity in the app's database.
 *
 * @param buildingID The unique identifier for the building entity (Primary Key).
 * @param img The resource ID of the building's image.
 * @param name The name of the building.
 * @param desc The description or details of the building.
 * @param cost The cost of the building as a String (formatted for display).
 * @param cps The amount of clips per second (cps) produced by the building.
 * @param amount The current number of buildings owned for this type.
 */
@Entity(tableName = "buildings")
data class buildingEntity(
    @PrimaryKey var buildingID: Int,
    @ColumnInfo(name = "buildingImg") var img: Int,
    @ColumnInfo(name = "buildingName") var name: String,
    @ColumnInfo(name = "buildingDesc") var desc: String,
    @ColumnInfo(name = "buildingCost") var cost: String,
    @ColumnInfo(name = "buildingCps") var cps: Int,
    @ColumnInfo(name = "buildingAmount") var amount: Int
)