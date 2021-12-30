package com.easv.esbjergensemblescanningapp.Data

import com.easv.esbjergensemblescanningapp.Model.BEScan

interface IScanDao {

    fun getScansByConcertId(concertId: Int): List<BEScan>

    fun insert(s: BEScan)
}