package com.danilo.evolveusers.ui.userdetails

import com.danilo.evolveusers.data.database.DBHelper
import com.danilo.evolveusers.data.database.Result
import com.danilo.evolveusers.data.model.User

class UserDetailsRepository {

    fun update(db: DBHelper, userModel: User): Result {
        return db.updateUser(userModel)
    }

    fun getUser(db: DBHelper, cpfCnpj: String): User? {
        return db.getUser(cpfCnpj)
    }
}