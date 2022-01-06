package com.easv.esbjergensemblescanningapp.Data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.easv.esbjergensemblescanningapp.Model.BEScan

class ScanDAO_Impl (context: Context) : SQLiteOpenHelper(context, DATABASE_SCAN, null, DATABASE_VERSION), IScanDAO {

    companion object {
        private const val DATABASE_VERSION = 4
        private const val DATABASE_SCAN = "Scan"
    }

    override fun onCreate(db: SQLiteDatabase?) { //Creates the table on runtime with the relevant table columns
        db?.execSQL("CREATE TABLE $DATABASE_SCAN (id INTEGER PRIMARY KEY, concertId INTEGER, userId INTEGER, securityCode TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { //drops the table in case the Db version is updated
        db!!.execSQL("DROP TABLE IF EXISTS $DATABASE_SCAN")
        onCreate(db)
    }

    override fun getScansByConcertId(concertId: Int): List<BEScan> {
        val scanList: ArrayList<BEScan> = ArrayList()
        val selectQuery = "SELECT  * FROM $DATABASE_SCAN WHERE concertId LIKE $concertId"
        val db = this.readableDatabase
        var cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id: Int
        var concertId: Int
        var userId: Int
        var securityCode: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                concertId = cursor.getInt(cursor.getColumnIndex("concertId"))
                userId = cursor.getInt(cursor.getColumnIndex("userId"))
                securityCode = cursor.getString(cursor.getColumnIndex("securityCode"))

                val neWScan = BEScan(
                    id = id,
                    concertId = concertId,
                    userId = userId,
                    securityCode = securityCode,
                )
                scanList.add(neWScan)
            } while (cursor.moveToNext())
        }
        return scanList
    }

    override fun insert(s: BEScan) {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put("concertId", s.concertId)
        cv.put("userId", s.userId)
        cv.put("securityCode", s.securityCode)

        db.insert("$DATABASE_SCAN", null, cv)
    }

    override fun deleteFromDb(concertId: Int) {
        val db = this.writableDatabase
        db!!.execSQL("DELETE FROM $DATABASE_SCAN WHERE concertId = $concertId")
    }
}