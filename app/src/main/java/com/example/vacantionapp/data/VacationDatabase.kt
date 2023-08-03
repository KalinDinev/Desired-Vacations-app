package com.example.vacantionapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.vacantionapp.model.DesiredVacation
import com.example.vacantionapp.utility.Converters
import com.example.vacantionapp.dao.VacationDao


@Database(entities = [DesiredVacation::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class VacationDatabase : RoomDatabase() {

    abstract fun vacationDao(): VacationDao

    companion object {
        @Volatile
        private var INSTANCE: VacationDatabase? = null

        fun getDataBase(context: Context): VacationDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VacationDatabase::class.java,
                    "vacation_table"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}