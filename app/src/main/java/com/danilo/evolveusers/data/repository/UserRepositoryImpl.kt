package com.danilo.evolveusers.data.repository

import com.danilo.evolveusers.data.model.UserResponse

class UserRepositoryImpl(/*private val apiService: RetrofitService*/): UserRepository {


    override suspend fun sendUser(): UserResponse? {
        TODO("Not yet implemented")
    }

    override suspend fun signup(id: Int): UserResponse? {
        TODO("Not yet implemented")
    }
}