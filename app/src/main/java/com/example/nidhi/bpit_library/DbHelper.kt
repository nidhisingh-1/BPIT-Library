package com.example.nidhi.bpit_library

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context) : SQLiteOpenHelper(
    context,
    "books.db",
    null,
    1
) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(BookTable.CMD_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
}
