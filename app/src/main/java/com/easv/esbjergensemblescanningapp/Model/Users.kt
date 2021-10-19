package com.easv.esbjergensemblescanningapp.Model

class Users {

    var listOfUsers = arrayListOf(
        BEUser(1, "username1", "password", "Rocio", "Tapia"),
        BEUser(2, "username2", "password", "Francesco", "Tesolato")
    )

    fun checkUserExists(username: String, password: String): BEUser? {
        var i = 0
        listOfUsers.forEach {
            when (username) {
                listOfUsers[i].username ->
                    when (password) {
                        listOfUsers[i].password ->
                            return listOfUsers[i]
                    }
            }
        }
        i++
        return null
    }

}