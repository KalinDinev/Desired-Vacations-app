package com.example.vacantionapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.vacantionapp.data.VacationDatabase
import com.example.vacantionapp.model.DesiredVacation
import com.example.vacantionapp.repository.VacationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VacationViewModel(application: Application) : AndroidViewModel(application) {


    val readAllData: LiveData<List<DesiredVacation>>
    private val repository: VacationRepository

    init {
        val vacationDao = VacationDatabase.getDataBase(application).vacationDao()
        repository = VacationRepository(vacationDao, application)
        readAllData = repository.readAllData
    }


    fun addVacation(vacation: DesiredVacation) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addVacation(vacation)
        }
    }

    fun findViewById(currentId:Int):LiveData<DesiredVacation>{
        return repository.findViewById(currentId)
    }

    fun deleteVacation(vacation: DesiredVacation) {
        viewModelScope.launch {
            repository.deleteVacation(vacation)
        }
    }

    fun updateVacation(vacation: DesiredVacation){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateVacation(vacation)
        }
    }
}