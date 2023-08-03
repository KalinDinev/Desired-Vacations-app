package com.example.vacantionapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.vacantionapp.dao.VacationDao
import com.example.vacantionapp.model.DesiredVacation

class VacationRepository(private val vacationDao: VacationDao, private val context: Application) {

    val readAllData: LiveData<List<DesiredVacation>> = vacationDao.readAllData()

    suspend fun addVacation(vacation: DesiredVacation) {
        vacationDao.addVacation(vacation)
    }

    suspend fun deleteVacation(vacation: DesiredVacation) {
        vacationDao.deleteVacation(vacation)
    }

    fun findViewById(currentId: Int) : LiveData<DesiredVacation> {
        return vacationDao.findViewById(currentId)
    }

    suspend fun updateVacation(vacation:DesiredVacation){
        vacationDao.updateVacation(vacation)
    }
}