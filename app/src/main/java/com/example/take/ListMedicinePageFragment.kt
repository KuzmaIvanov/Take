package com.example.take

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.take.model.Medicament
import com.example.take.model.MedicamentsListener
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListMedicinePageFragment : Fragment() {

    private lateinit var adapter: MedicamentsAdapter
    private val viewModel: ListMedicineViewModel by viewModels { factory() }
    private lateinit var appNavigator: AppNavigator
    private lateinit var listMedicineRecyclerView: RecyclerView
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
        listMedicineRecyclerView = view.findViewById(R.id.listMedicineRecyclerView)
        listMedicineRecyclerView.layoutManager = layoutManager
        listMedicineRecyclerView.adapter = adapter
        setItemTouchHelper()

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
            val pendingIntent = PendingIntent.getBroadcast(requireContext(), medicament.id.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
            if(pendingIntent!=null) {
                alarmManager.cancel(pendingIntent)
            }
            actionIncrement++
        }
        Toast.makeText(requireContext(), "Alarm cancelled successfully", Toast.LENGTH_SHORT).show()
    }

    private fun setItemTouchHelper() {

        ItemTouchHelper(object : ItemTouchHelper.Callback() {

            private val limitScrollX = dipToPx(90f, requireContext()) //150dp
            private var currentScrollX = 0
            private var currentScrollXWhenInActive = 0
            private var initXWhenInActive = 0f
            private var firstInActive = false

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = 0
                val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return Integer.MAX_VALUE.toFloat()
            }

            override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
                return Integer.MAX_VALUE.toFloat()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    if(dX == 0f) {
                        currentScrollX = viewHolder.itemView.scrollX
                        firstInActive = true
                    }
                    if(isCurrentlyActive) {
                        //swipe with finger
                        var scrollOffset = currentScrollX + (-dX).toInt()
                        if(scrollOffset > limitScrollX) {
                            scrollOffset = limitScrollX
                        }
                        else if (scrollOffset < 0) {
                            scrollOffset = 0
                        }
                        viewHolder.itemView.scrollTo(scrollOffset, 0)
                    }
                    else {
                        //swipe with auto animation
                        if(firstInActive) {
                            firstInActive = false
                            currentScrollXWhenInActive = viewHolder.itemView.scrollX
                            initXWhenInActive = dX
                        }

                        if(viewHolder.itemView.scrollX < limitScrollX) {
                            viewHolder.itemView.scrollTo((currentScrollXWhenInActive * dX / initXWhenInActive).toInt(), 0)
                        }
                    }
                }
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)

                val medicament: Medicament = viewHolder.itemView.tag as Medicament
                if(viewHolder.itemView.scrollX == limitScrollX) {
                    viewModel.deleteMedicament(medicament)
                    cancelAlarm(medicament)
                }
            }

        }).apply {
            attachToRecyclerView(listMedicineRecyclerView)
        }

    }

    private fun dipToPx(dipValue: Float, context: Context): Int {
        return (dipValue*context.resources.displayMetrics.density).toInt()
    }

    private val medicamentListener: MedicamentsListener = {
        adapter.medicaments = it
    }
}