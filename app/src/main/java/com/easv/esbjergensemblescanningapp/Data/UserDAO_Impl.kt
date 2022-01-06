package com.easv.esbjergensemblescanningapp.Data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.easv.esbjergensemblescanningapp.Model.BEUser

class UserDAO_Impl (context: Context) : SQLiteOpenHelper(context, DATABASE_USER, null, DATABASE_VERSION), IUserDAO {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_USER = "User"
    }

    override fun onCreate(db: SQLiteDatabase?) { //Creates the table on runtime with the relevant table columns
        db?.execSQL("CREATE TABLE ${UserDAO_Impl.DATABASE_USER} (id INTEGER PRIMARY KEY, code INTEGER, firstName INTEGER, lastName TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { //drops the table in case the Db version is updated
        db!!.execSQL("DROP TABLE IF EXISTS ${UserDAO_Impl.DATABASE_USER}")
        onCreate(db)
    }

    override fun login(code: Int): BEUser? {
        val db = this.readableDatabase
        var cursor = db.query("User",arrayOf("id","code","firstName","lastName"),
                    "code LIKE '$code'",
                    null,null, null,null)
        var myList: List<BEUser> = getByCursor(cursor)
        if (myList.isNotEmpty()) {
            return myList[0]
        } else {
            return null
        }
    }

    private fun getByCursor(cursor: Cursor): List<BEUser> {
        val myList = ArrayList<BEUser>()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val code = cursor.getInt(cursor.getColumnIndex("code"))
                val firstName = cursor.getString(cursor.getColumnIndex("firstName"))
                val lastName = cursor.getString(cursor.getColumnIndex("lastName"))
                myList.add(BEUser(id, code, firstName, lastName))
            } while (cursor.moveToNext())
        }
        return myList
    }

    override fun insert(u: BEUser) {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put("id", u.id)
        cv.put("code", u.code)
        cv.put("firstName", u.firstName)
        cv.put("lastName", u.lastName)

        db.insert("${DATABASE_USER}", null, cv)
    }
}