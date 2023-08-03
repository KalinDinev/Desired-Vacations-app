package com.example.vacantionapp.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vacation_table")
 data class DesiredVacation(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val hotelName:String,
    val location:String,
    val necessaryMoneyAmount: String,
    val description:String,
    val photo:Bitmap

 )