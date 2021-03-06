package com.easv.esbjergensemblescanningapp.Model.Mock

import com.easv.esbjergensemblescanningapp.Model.BEUser

class User {

    private var listOfUsers = arrayListOf(
        BEUser(1, 1111, "Rocio", "Tapia"),
        BEUser(2, 2222, "Francesco", "Tesolato")
    )

    fun checkUserExists(code: Int): Boolean {
        var i = 0
        listOfUsers.forEach {
            when (code) {
                listOfUsers[i].code -> return true
            }
            i++
        }
        return false
    }
}