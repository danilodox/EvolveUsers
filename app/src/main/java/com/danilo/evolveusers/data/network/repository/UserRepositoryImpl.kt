package com.danilo.evolveusers.data.network.repository

import com.danilo.evolveusers.data.model.User
import com.danilo.evolveusers.data.network.RetrofitService

class UserRepositoryImpl(private val apiService: RetrofitService): UserRepository {

    override suspend fun postUser(user: User) = apiService.service.postUser(user)
}