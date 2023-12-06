package com.danilo.evolveusers.data.network.repository

import com.danilo.evolveusers.data.model.User
import retrofit2.Response
import retrofit2.http.POST


interface UserRepository {

suspend fun postUser(user: User): Response<User>
}