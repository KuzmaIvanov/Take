package com.example.take.model

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.provider.BaseColumns
import android.widget.Toast
import com.example.take.AlarmReceiver
import com.example.take.MedicamentNotFoundException
import com.example.take.db.MyDbHelper
import com.example.take.db.MyDbNameClass

typealias MedicamentsListener = (medicaments: List<Medicament>) -> Unit

class MedicamentService(context: Context) {

    private val ourContext: Context = context
    private var medicaments = mutableListOf<Medicament>()
    private val myDbHelper = MyDbHelper(context)
    private lateinit var alarmManager: AlarmManager

    init {
        val db = myDbHelper.readableDatabase
        val projection = arrayOf(BaseColumns._ID, MyDbNameClass.COLUMN_NAME, MyDbNameClass.COLUMN_TIME)
        val cursor = db.query(MyDbNameClass.TABLE_NAME, projection, null, null, null, null, null)
        with(cursor) {
            while (moveToNext()) {
                //val medicamentId = getColumnIndexOrThrow(BaseColumns._ID)
                val medicamentId = getLong(0)
                val medicamentName = getString(1)
                val medicamentTime = parseTimeAsStringToListTime(getString(2))
                medicaments.add(Medicament(medicamentId, medicamentName, medicamentTime))
            }
        }
        cursor.close()
        db.close()
        createNotificationChannel()
    }

    private fun parseTimeAsStringToListTime(timeAsString: String):List<String> {
        return timeAsString.subSequence(0, timeAsString.length-1).split(" ")
    }

    private fun parseListTimeToTimeAsString(listTime: List<String>): String {
        val timeAsString = StringBuilder()
        listTime.forEach {
            timeAsString.append(it)
            timeAsString.append(" ")
        }
        return timeAsString.toString()
    }

    private val listeners = mutableSetOf<MedicamentsListener>()

    fun getMedicaments(): List<Medicament> {
        return medicaments
    }

    fun getById(id: Long): MedicamentDetails {
        val medicament = medicaments.firstOrNull{ it.id == id } ?: throw MedicamentNotFoundException()
        return MedicamentDetails(
            medicament = medicament,
            details = "More detail information"
        )
    }

    fun addMedicament(medicamentDetails: MedicamentDetails, listCalendar: MutableList<Calendar>) {
        val db = myDbHelper.writableDatabase
        val values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME, medicamentDetails.medicament.name)
            put(MyDbNameClass.COLUMN_TIME, parseListTimeToTimeAsString(medicamentDetails.medicament.time))
            put(MyDbNameClass.COLUMN_DESCRIPTION, medicamentDetails.details)
        }
        val newId = db.insert(MyDbNameClass.TABLE_NAME, null, values)
        db.close()
        medicaments = ArrayList(medicaments)
        medicaments.add(Medicament(newId, medicamentDetails.medicament.name, medicamentDetails.medicament.time))
        notifyChanges()
        setAlarm(listCalendar, newId, medicamentDetails.medicament.name)
    }


    fun deleteMedicament(medicament: Medicament) {
        val indexToDelete = medicaments.indexOfFirst { it.id == medicament.id }
        if(indexToDelete!=-1) {
            medicaments = ArrayList(medicaments)
            medicaments.removeAt(indexToDelete)
            notifyChanges()
        }
    }

    fun addListener(listener: MedicamentsListener) {
        listeners.add(listener)
        listener.invoke(medicaments)
    }

    fun removeListener(listener: MedicamentsListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        listeners.forEach { it.invoke(medicaments) }
    }

    private fun setAlarm(listCalendar: MutableList<Calendar>,idMedicamentToAddAlarm: Long, medicamentName: String) {
        alarmManager = ourContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        var actionIncrement = 1;
        listCalendar.forEach {
            val intent = Intent(ourContext, AlarmReceiver::class.java)
            intent.putExtra("medicamentName", medicamentName)
            intent.action = "action$actionIncrement"
            val pendingIntent = PendingIntent.getBroadcast(ourContext, idMedicamentToAddAlarm.toInt(), intent, 0)
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, it.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
            actionIncrement++
        }
        Toast.makeText(ourContext, "Alarm set successfully", Toast.LENGTH_SHORT).show()
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel("alarm_receiver_id",
                "alarm_receiver_channel_name",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = ourContext.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}