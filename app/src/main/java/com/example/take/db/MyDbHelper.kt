package com.example.take.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDbHelper(context: Context):
    SQLiteOpenHelper(context, MyDbNameClass.DATABASE_NAME, null, MyDbNameClass.DATABASE_VERSION) {


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(MyDbNameClass.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //самая простейшеая реализация, в будущем нужно будет написать логику при изменеии версии БД
        db.execSQL(MyDbNameClass.DELETE_TABLE)
        onCreate(db)
    }
}