package com.example.take

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.take.databinding.ActivityMainBinding
import com.example.take.model.Medicament

class MainActivity : AppCompatActivity(), AppNavigator {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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