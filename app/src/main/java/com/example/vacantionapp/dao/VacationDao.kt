package com.example.vacantionapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.vacantionapp.model.DesiredVacation


@Dao
interface VacationDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addVacation(vacation:DesiredVacation)

    @Query("SELECT * FROM  vacation_table ORDER BY  id ASC")
     fun readAllData() : LiveData<List<DesiredVacation>>


     @Query("SELECT * FROM vacation_table WHERE id LIKE :currentId")
     fun findViewById(currentId:Int) : LiveData<DesiredVacation>

     @Update
     suspend fun updateVacation(vacation:DesiredVacation)


     @Delete
     suspend fun deleteVacation(vacation: DesiredVacation)
}