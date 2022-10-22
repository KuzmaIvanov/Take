package com.example.take

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.take.databinding.FragmentMedicamentDetailsPageBinding
import com.example.take.model.TimeRecylerAdapter


class MedicamentDetailsPageFragment : Fragment() {

    private var _binding: FragmentMedicamentDetailsPageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MedicamentDetailsViewModel by viewModels { factory() }
    private lateinit var appNavigator: AppNavigator
    private val args: MedicamentDetailsPageFragmentArgs by navArgs()

    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appNavigator = context as AppNavigator
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadMedicament(args.clickedMedicamentId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMedicamentDetailsPageBinding.inflate(layoutInflater, container, false)
        viewModel.medicamentDetails.observe(viewLifecycleOwner, Observer {
            binding.nameMedicamentTextViewDetails.text = it.medicament.name
            binding.moreDetailInformationTextViewDetails.text = it.details
            binding.timeMedicamentRecyclerViewDetails.adapter = TimeRecylerAdapter(it.medicament.time)
        })
        binding.removeMedicamentBtnDetails.setOnClickListener{
            viewModel.deleteMedicament()
            appNavigator.navigateToListMedicinePageFromDetailsPage()
        }
        return binding.root
    }

    private fun cancelAlarm(idMedicamentToCancelAlarm: Long) {
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(requireContext(), idMedicamentToCancelAlarm.toInt(), intent, 0)
        alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.cancel(pendingIntent)
        Toast.makeText(requireContext(), "Alarm cancelled successfully", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}