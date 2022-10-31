package com.example.take

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.take.model.MedicamentDetails
import com.example.take.model.MedicamentService

class MedicamentDetailsViewModel(
    private val medicamentService: MedicamentService
): ViewModel() {
    private val _medicamentDetails = MutableLiveData<MedicamentDetails>()
    val medicamentDetails : LiveData<MedicamentDetails> = _medicamentDetails

    fun loadMedicament(medicamentId: Long) {
        if(_medicamentDetails.value != null) return
        _medicamentDetails.value = medicamentService.getById(medicamentId)
    }

    fun deleteMedicament() {
        val medicamentDetails = this.medicamentDetails.value ?: return
        medicamentService.deleteMedicament(medicamentDetails.medicament)
    }
}