package com.easv.esbjergensemblescanningapp.Model

class User {

    var listOfUsers = arrayListOf(
        BEUser(1, "1111", "Rocio", "Tapia"),
        BEUser(2, "2222", "Francesco", "Tesolato")
    )

    fun checkUserExists(code: String): BEUser? {
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

/*    fun checkCodeExists(code: String): Boolean {
        var i = 0
        listOfUsers.forEach {
            when (code) {
                listOfUsers[i].code ->
                    return true
            }
        }
        i++
        return false
    }*/

}