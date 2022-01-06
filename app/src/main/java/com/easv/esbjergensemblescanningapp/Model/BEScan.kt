package com.easv.esbjergensemblescanningapp.Model

import java.io.Serializable

class BEScan (
    var id: Int,
    var concertId: Int,
    var userId: Int,
    var securityCode: String
) : Serializable