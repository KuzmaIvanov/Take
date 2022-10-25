package com.example.take.db

import android.provider.BaseColumns

object MyDbNameClass: BaseColumns {
    const val TABLE_NAME = "medicament_details"
    const val COLUMN_NAME = "name"
    const val COLUMN_TIME = "time"
    const val COLUMN_DESCRIPTION = "description"

    const val DATABASE_VERSION = 1;
    const val DATABASE_NAME = "medicaments.db"

    const val CREATE_TABLE =
        "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "$COLUMN_NAME TEXT," +
                "$COLUMN_TIME TEXT," +
                "$COLUMN_DESCRIPTION TEXT)"

    const val DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}