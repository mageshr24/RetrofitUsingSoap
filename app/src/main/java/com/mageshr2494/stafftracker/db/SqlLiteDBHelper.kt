package com.mageshr2494.stafftracker.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.sql.Types.NULL

class SqlLiteDBHelper(context: Context) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME, null,
        DATABASE_VERSION
    ) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "StaffTrackerDatabase"
        private val TABLE_NAME = "userlatlong"
        private val KEY_ID = "id"
        private val KEY_LATITUDE = "latitude"
        private val KEY_LONGITUDE = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val createTable =
            ("CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_LATITUDE + " REAL, " + KEY_LONGITUDE + " REAL" + ")")

        Log.v("createTableQuery", "" + createTable)
        db?.execSQL(createTable)
    }

    fun addLatLong(latitude: Double, longitude: Double) {

        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_LATITUDE, latitude)
        contentValues.put(KEY_LONGITUDE, longitude)

//        val sqlInsert = ("INSERT INTO " + TABLE_NAME + "(" + KEY_ID + " ," + KEY_LATITUDE + " ," + KEY_LONGITUDE + ") VALUES (null ," + latitude + "," + longitude + ")")
//        db.execSQL(sqlInsert)

        val success = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return
    }

    fun getData(): List<LatLongReq> {

        val latLongList: ArrayList<LatLongReq> = ArrayList<LatLongReq>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.writableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var latitude: Double
        var longitude: Double
        var id: Int

        if (cursor.moveToFirst()) {

            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                latitude = cursor.getDouble(cursor.getColumnIndex("latitude"))
                longitude = cursor.getDouble(cursor.getColumnIndex("longitude"))

                val datavalue = LatLongReq(id, latitude, longitude)
                latLongList.add(datavalue)

            } while (cursor.moveToNext())
        }
        return latLongList
    }

    fun deleteData(data: LatLongReq): Int {

        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, data.id) // EmpModelClass UserId
        // Deleting Row
        val success = db.delete(TABLE_NAME, "id=" + data.id, null)
        db.close() // Closing database connection
        return success

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

}