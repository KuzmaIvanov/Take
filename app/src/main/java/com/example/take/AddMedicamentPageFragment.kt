package com.example.take

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.viewModels
import com.example.take.databinding.FragmentAddMedicamentPageBinding
import com.example.take.model.Medicament
import com.example.take.model.MedicamentDetails
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat


class AddMedicamentPageFragment : Fragment() {

    private var _binding: FragmentAddMedicamentPageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListMedicineViewModel by viewModels { factory() }
    private var increment = 1
        get() = field++
    private var lastId = 0
    private val listOfTimeTextView: MutableList<TextView> = ArrayList()
    private lateinit var picker: MaterialTimePicker
    private lateinit var appNavigator: AppNavigator
    private val listCalendar: MutableList<Calendar> = ArrayList()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appNavigator = context as AppNavigator
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddMedicamentPageBinding.inflate(layoutInflater, container, false)
        binding.addTimeBtn.setOnClickListener {
            openTimePicker()
        }
        binding.addMedicament.setOnClickListener {
            addNewMedicament()
        }
        return binding.root
    }
    private fun openTimePicker() {
        val isSystem24hour = is24HourFormat(requireContext())
        val clockFormat = if(isSystem24hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(0)
            .setMinute(0)
            .setTitleText("Select time")
            .build()
        picker.show(childFragmentManager, "time_picker")
        picker.addOnPositiveButtonClickListener {
            val layout = binding.addMoreTimeConstraintLayout.root
            val timeTextView = TextView(requireContext())
            timeTextView.id = increment
            if(picker.minute<10) {
                timeTextView.text = picker.hour.toString()+":0"+picker.minute.toString()
            }
            else {
                timeTextView.text = picker.hour.toString()+":"+picker.minute.toString()
            }
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, picker.hour)
            calendar.set(Calendar.MINUTE, picker.minute)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0);
            listCalendar.add(calendar)
            listOfTimeTextView.add(timeTextView)
            layout.addView(timeTextView)
            val set = ConstraintSet()
            set.clone(layout)
            timeTextView.id.also {
                set.connect(it, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                if (lastId == 0)
                    set.connect(it, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                else
                    set.connect(it, ConstraintSet.TOP, lastId, ConstraintSet.BOTTOM)
                lastId = it
            }
            set.applyTo(layout)
        }
    }

    private fun addNewMedicament() {
        if(listOfTimeTextView.size==0) {
            Toast.makeText(requireContext(), "Please enter name of medicament and add time if necessary", Toast.LENGTH_SHORT).show()
        }
        else {
            val enteredNameMedicament = binding.enterMedicamentNameEditText.text.toString()
            val enteredDescriptionMedicament = binding.enterMedicamentDescriptionEditText.text.toString()
            if(enteredNameMedicament != "") {
                val listOfStringTime = ArrayList(listOfTimeTextView)
                val medicament = Medicament(-1, enteredNameMedicament, listOfStringTime.map { it -> it.text.toString() })
                val medicamentDetails = MedicamentDetails(medicament, enteredDescriptionMedicament)
                viewModel.addMedicament(medicamentDetails, listCalendar)
                appNavigator.navigateToListMedicinePageFromAddPage()
            }
            else {
                Toast.makeText(requireContext(), "Please enter name of medicament and add time if necessary", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}