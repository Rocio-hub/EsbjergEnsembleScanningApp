package com.easv.esbjergensemblescanningapp.Model

import androidx.room.Entity

@Entity
data class Scan(var id: Int,
                            var concertId: Int,
                            var userId: Int,
                            var secutiryCode: String)