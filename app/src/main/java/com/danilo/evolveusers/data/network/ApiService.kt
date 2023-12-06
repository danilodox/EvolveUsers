package com.danilo.evolveusers.data.network

import com.danilo.evolveusers.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("Desafio/rest/desafioRest")
    suspend fun postUser(@Body post: User): Response<User>
}