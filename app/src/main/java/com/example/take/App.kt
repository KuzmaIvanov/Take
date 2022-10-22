package com.example.take

import android.app.AlarmManager
import android.app.Application
import com.example.take.model.MedicamentService

class App: Application() {
    val medicamentService = MedicamentService()
}