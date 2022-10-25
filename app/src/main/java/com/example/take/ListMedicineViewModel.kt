package com.example.take

import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.take.model.Medicament
import com.example.take.model.MedicamentDetails
import com.example.take.model.MedicamentService
import com.example.take.model.MedicamentsListener

class ListMedicineViewModel(
    private val medicamentService: MedicamentService
) : ViewModel() {

    private val _medicaments = MutableLiveData<List<Medicament>>()
    val medicaments: LiveData<List<Medicament>> = _medicaments
    private val listener: MedicamentsListener = {
        _medicaments.value = it
    }

    init {
        loadMedicine()
    }

    fun loadMedicine() {
        medicamentService.addListener(listener)
    }

    fun deleteMedicament(medicament: Medicament) {
        medicamentService.deleteMedicament(medicament)
    }

    fun addMedicament(medicamentDetails: MedicamentDetails, listCalendar: MutableList<Calendar>) {
        medicamentService.addMedicament(medicamentDetails, listCalendar)
    }

    override fun onCleared() {
        super.onCleared()
        medicamentService.removeListener(listener)
    }
}