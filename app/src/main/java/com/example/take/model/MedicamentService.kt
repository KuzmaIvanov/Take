package com.example.take.model

import com.example.take.MedicamentNotFoundException

typealias MedicamentsListener = (medicaments: List<Medicament>) -> Unit

class MedicamentService {

    private var medicaments = mutableListOf<Medicament>(
        Medicament(1L,"dsdqwew", listOf("10:00", "9:00")),
        Medicament(2L, "wqsdwdw", listOf("20:30", "21:30", "12:00")),
        Medicament(3L, "sdwawd", listOf("19:00"))
    )
    private val listeners = mutableSetOf<MedicamentsListener>()

    fun getMedicaments(): List<Medicament> {
        return medicaments
    }

    fun getById(id: Long): MedicamentDetails {
        val medicament = medicaments.firstOrNull{ it.id == id } ?: throw MedicamentNotFoundException()
        return MedicamentDetails(
            medicament = medicament,
            details = "More detail information"
        )
    }

    fun addMedicament(medicament: Medicament) {
        medicaments = ArrayList(medicaments)
        medicaments.add(medicament)
        notifyChanges()
    }

    fun deleteMedicament(medicament: Medicament) {
        val indexToDelete = medicaments.indexOfFirst { it.id == medicament.id }
        if(indexToDelete!=-1) {
            medicaments = ArrayList(medicaments)
            medicaments.removeAt(indexToDelete)
            notifyChanges()
        }
    }

    fun addListener(listener: MedicamentsListener) {
        listeners.add(listener)
        listener.invoke(medicaments)
    }

    fun removeListener(listener: MedicamentsListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        listeners.forEach { it.invoke(medicaments) }
    }
}