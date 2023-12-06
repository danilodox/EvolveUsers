package com.danilo.evolveusers.ui.signup

import com.danilo.evolveusers.data.database.DBHelper
import com.danilo.evolveusers.data.database.Result
import com.danilo.evolveusers.data.model.User

class SignupRepository() {


    fun signup(db: DBHelper, userModel: User): Result {
        return db.insertData(
            userModel
        )
    }


}