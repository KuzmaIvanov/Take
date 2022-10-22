package com.example.take

import com.example.take.model.Medicament

interface AppNavigator {
    fun navigateToListMedicinePage()
    fun navigateMedicamentDetailsPage(medicament: Medicament)
    fun navigateToListMedicinePageFromDetailsPage()
    fun navigateToAddMedicamentPage()
    fun navigateToListMedicinePageFromAddPage()
}