package com.example.take

import android.app.Application
import android.content.Context
import com.example.take.model.MedicamentService

class App: Application() {
    lateinit var medicamentService: MedicamentService
    override fun onCreate() {
        super.onCreate()
        medicamentService = MedicamentService(this)
    }
}