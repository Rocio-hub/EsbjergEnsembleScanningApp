package com.easv.esbjergensemblescanningapp.Data

import com.easv.esbjergensemblescanningapp.Model.BEUser

interface IUserDAO {

    //Local database table with info about users that can log into this app
    fun login (code: Int): BEUser?

    fun insert(user: BEUser)

}