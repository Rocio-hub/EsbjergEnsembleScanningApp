package com.easv.esbjergensemblescanningapp.Data

import com.easv.esbjergensemblescanningapp.Model.BEUser

interface IUserDAO {

    fun login (code: Int): BEUser?

    fun insert(user: BEUser)
}