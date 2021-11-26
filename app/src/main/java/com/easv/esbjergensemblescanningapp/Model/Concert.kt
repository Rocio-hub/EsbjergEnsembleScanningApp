package com.easv.esbjergensemblescanningapp.Model

class Concert {

    var listOfConcerts = arrayListOf(
        BEConcert(1, "Concert1", "01/01/2001", "01:01:01"),
        BEConcert(2, "Concert2", "02/02/2002", "02:02:02"),
        BEConcert(3, "Concert3", "03/03/2003", "03:03:03"),
    )

    fun getAllConcerts(): List<BEConcert> {
        return listOfConcerts
    }

    fun getEventById(id: Int): BEConcert {
        for(i in listOfConcerts) {
            when (id) {
                i.id ->
                    return i
            }
        }
        return BEConcert(0, "", "", "")
    }
}