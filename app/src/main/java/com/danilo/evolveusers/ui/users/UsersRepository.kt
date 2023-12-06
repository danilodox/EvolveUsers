package com.danilo.evolveusers.ui.users

import com.danilo.evolveusers.data.database.DBHelper
import com.danilo.evolveusers.data.database.Result
import com.danilo.evolveusers.data.model.User
import com.danilo.evolveusers.ui.users.UserResponse

class UsersRepository {

    fun getAllUsers(db: DBHelper): ArrayList<User> {
        return db.getAllUsers()
    }
}