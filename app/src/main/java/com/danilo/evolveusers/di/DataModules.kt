package com.danilo.evolveusers.di

import com.danilo.evolveusers.data.database.DBHelper
import com.danilo.evolveusers.data.network.RetrofitService
import com.danilo.evolveusers.data.network.repository.UserRepository
import com.danilo.evolveusers.data.network.repository.UserRepositoryImpl
import com.danilo.evolveusers.ui.signup.SignupRepository
import com.danilo.evolveusers.ui.userdetails.UserDetailsRepository
import com.danilo.evolveusers.ui.users.UsersRepository
import org.koin.dsl.module

val dataModule = module {
    single { DBHelper(get()) }

    single { SignupRepository()}

    single { UsersRepository() }
    single { UserDetailsRepository() }

    factory <UserRepository> { UserRepositoryImpl (get()) }

    factory { RetrofitService() }
}