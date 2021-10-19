package com.easv.esbjergensemblescanningapp.Model

class Users {

    var listOfUsers = arrayListOf(
        BEUser(1, 1111, "Rocio", "Tapia"),
        BEUser(2, 2222, "Francesco", "Tesolato")
    )

    fun checkUserExists(code: Int): BEUser? {
        var i = 0
        listOfUsers.forEach {
            when (code) {
                listOfUsers[i].code ->
                            return listOfUsers[i]
            }
        }
        i++
        return null
    }

}