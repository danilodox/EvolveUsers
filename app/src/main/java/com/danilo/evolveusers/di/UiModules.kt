package com.danilo.evolveusers.di

import com.danilo.evolveusers.ui.signup.SignupViewModel
import com.danilo.evolveusers.ui.userdetails.UserDetailsViewModel
import com.danilo.evolveusers.ui.users.UsersViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val uiModule = module {
    viewModel {SignupViewModel( get(), get() ) }
    viewModel {UsersViewModel( get() ) }
    viewModel {UserDetailsViewModel( get() )}
}