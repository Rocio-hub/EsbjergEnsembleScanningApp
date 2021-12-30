package com.easv.esbjergensemblescanningapp.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Scan(@PrimaryKey var id: Int,
                            var concertId: Int,
                            var code: String)