package com.easv.esbjergensemblescanningapp.Data

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.easv.esbjergensemblescanningapp.Model.BEUser

class UserDAO_Impl(context: Context) :
    SQLiteOpenHelper(context, "ScannerAppDB", null, 20), IUserDAO {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE AdminUser (id INTEGER PRIMARY KEY, username TEXT, password TEXT, firstName TEXT, lastName TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + "AdminUser")
        onCreate(db)
    }

    override fun login(username: String, password: String): BEUser? {
        val db = this.readableDatabase
        var cursor = db.query(
            "AdminUser",
            arrayOf(
                "id",
                "username",
                "password",
                "firstname",
                "lastname"
            ),
            "username LIKE '$username' AND password LIKE '$password'",
            null,
            null,
            null,
            null
        )
        var userList: List<BEUser> = getUserByCursor(cursor)
        if(userList.isNotEmpty()) {
            return userList[0]
        } else {
        return null
        }
    }

    private fun getUserByCursor(cursor: Cursor): List<BEUser> {
        val userList = ArrayList<BEUser>()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val code = cursor.getString(cursor.getColumnIndex("code"))
                val firstName = cursor.getString(cursor.getColumnIndex("firstName"))
                val lastName = cursor.getString(cursor.getColumnIndex("lastName"))
                userList.add(
                    BEUser(
                        id,
                        code,
                        firstName,
                        lastName,
                    )
                )
            } while (cursor.moveToNext())
        }
        return userList
    }

}