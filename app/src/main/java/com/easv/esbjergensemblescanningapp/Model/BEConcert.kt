package com.easv.esbjergensemblescanningapp.Model

import java.io.Serializable

class BEConcert(
    var id: Int,
    var title: String,
    var date: String,
    var time: String
) : Serializable