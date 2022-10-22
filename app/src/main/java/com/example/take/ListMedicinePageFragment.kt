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
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.take.model.Medicament
import com.example.take.model.MedicamentsListener
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListMedicinePageFragment : Fragment() {

    private lateinit var adapter: MedicamentsAdapter
    private val viewModel: ListMedicineViewModel by viewModels { factory() }
    private lateinit var appNavigator: AppNavigator

    private lateinit var alarmManager: AlarmManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appNavigator = context as AppNavigator
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_medicine_page, container, false)
        adapter = MedicamentsAdapter(object: MedicamentActionListener {
            override fun onMedicamentDelete(medicament: Medicament) {
                viewModel.deleteMedicament(medicament)
                cancelAlarm(medicament)
            }

            override fun onMedicamentDetails(medicament: Medicament) {
                appNavigator.navigateMedicamentDetailsPage(medicament)
            }
        })

        viewModel.medicaments.observe(viewLifecycleOwner, Observer {
            adapter.medicaments = it
        })

        val layoutManager = LinearLayoutManager(requireContext())
        val listMedicineRecyclerView: RecyclerView = view.findViewById(R.id.listMedicineRecyclerView)
        listMedicineRecyclerView.layoutManager = layoutManager
        listMedicineRecyclerView.adapter = adapter
        val itemAnimator = listMedicineRecyclerView.itemAnimator
        if(itemAnimator is DefaultItemAnimator)
            itemAnimator.supportsChangeAnimations = false

        val floatingActionButton: FloatingActionButton = view.findViewById(R.id.floatingActionBtn)
        floatingActionButton.setOnClickListener {
            appNavigator.navigateToAddMedicamentPage()
        }

        return view
    }

    private fun cancelAlarm(medicament: Medicament) {
        var actionIncrement = 1;
        alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        while(actionIncrement<=medicament.time.size) {
            val intent = Intent(requireContext(), AlarmReceiver::class.java)
            intent.action = "action$actionIncrement"
            val pendingIntent = PendingIntent.getBroadcast(requireContext(), medicament.id.toInt(), intent, 0)
            if(pendingIntent!=null) {
                alarmManager.cancel(pendingIntent)
            }
            actionIncrement++
        }
        Toast.makeText(requireContext(), "Alarm cancelled successfully", Toast.LENGTH_SHORT).show()
    }

    private val medicamentListener: MedicamentsListener = {
        adapter.medicaments = it
    }
}