package com.danilo.evolveusers.data.repository

import com.danilo.evolveusers.data.model.UserResponse

interface UserRepository {

    suspend fun sendUser(): UserResponse?

    suspend fun signup(id : Int): UserResponse?
}