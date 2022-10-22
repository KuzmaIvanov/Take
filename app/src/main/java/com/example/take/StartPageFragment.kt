package com.example.take

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class StartPageFragment : Fragment() {

    private lateinit var appNavigator: AppNavigator

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appNavigator = context as AppNavigator
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_start_page, container, false)
        val startTreatmentBtn: Button = view.findViewById(R.id.startTreatmentBtn)

        startTreatmentBtn.setOnClickListener {
            appNavigator.navigateToListMedicinePage()
        }

        return view
    }
}