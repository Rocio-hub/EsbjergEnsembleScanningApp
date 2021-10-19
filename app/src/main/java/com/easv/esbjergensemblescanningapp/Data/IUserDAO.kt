package com.easv.esbjergensemblescanningapp.Data

import com.easv.esbjergensemblescanningapp.Model.BEUser

interface IUserDAO {

    //Local database table with info about users that can log into this app

    //Should we be able to create new admin users?

    fun login (username: String, password: String): BEUser?
}