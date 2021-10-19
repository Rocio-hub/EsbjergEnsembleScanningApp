package com.easv.esbjergensemblescanningapp.Model

class Events {

    var listOfEvents = arrayListOf(
        BEEvent(1, "Concert1"),
        BEEvent(2, "Concert2"),
        BEEvent(3, "Concert3"),
    )

    fun getAllEvents(): List<BEEvent> {
        return listOfEvents
    }

    fun getEventById(id: Int): BEEvent {
        for(i in listOfEvents) {
            when (id) {
                i.id ->
                    return i
            }
        }
        return BEEvent(0, "")
    }
}