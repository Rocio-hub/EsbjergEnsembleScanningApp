package com.easv.esbjergensemblescanningapp.Model.Mock

import com.easv.esbjergensemblescanningapp.Model.BEScan

class Scan {
    private var listOfScans = arrayListOf(
        BEScan(1, 123, 111, "abc"),
        BEScan(2, 123, 111, "def"),
        BEScan(3, 234, 222, "ghi"),
    )

    fun getAllScans(): List<BEScan> {
        return listOfScans
    }
}