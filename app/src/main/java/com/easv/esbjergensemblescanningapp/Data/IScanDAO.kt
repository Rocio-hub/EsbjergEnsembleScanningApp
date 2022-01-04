package com.easv.esbjergensemblescanningapp.Data

import com.easv.esbjergensemblescanningapp.Model.BEScan

interface IScanDAO {

    fun insert(scan: BEScan)

    fun getScansByConcertId(concertId: Int): List<BEScan>

    fun deleteFromDb(concertId: Int)
}