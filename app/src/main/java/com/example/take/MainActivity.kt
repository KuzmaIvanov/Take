package com.example.take

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.example.take.databinding.ActivityMainBinding
import com.example.take.model.Medicament
import com.google.android.material.color.DynamicColors

class MainActivity : AppCompatActivity(), AppNavigator {

    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        DynamicColors.applyToActivityIfAvailable(this)
        setContentView(binding.root)
    }

    override fun navigateToListMedicinePage() {
        val action = StartPageFragmentDirections.actionStartPageFragmentToListMedicinePageFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateMedicamentDetailsPage(medicament: Medicament) {
        val action = ListMedicinePageFragmentDirections.actionListMedicinePageFragmentToMedicamentDetailsPageFragment(medicament.id)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }


    override fun navigateToListMedicinePageFromDetailsPage() {
        val action = MedicamentDetailsPageFragmentDirections.actionMedicamentDetailsPageFragmentToListMedicinePageFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateToAddMedicamentPage() {
        val action = ListMedicinePageFragmentDirections.actionListMedicinePageFragmentToAddMedicamentPageFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateToListMedicinePageFromAddPage() {
        val action = AddMedicamentPageFragmentDirections.actionAddMedicamentPageFragmentToListMedicinePageFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

}