package com.example.appdevprojectradlerprueller
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigInteger


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