package com.danilo.evolveusers.ui.users

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilo.evolveusers.data.database.DBHelper
import com.danilo.evolveusers.data.model.User
import com.danilo.evolveusers.data.repository.UserRepository
import kotlinx.coroutines.launch

class UsersViewModel(private val repository: UsersRepository) : ViewModel() {


    val mUsersLiveData : MutableLiveData<ArrayList<User>> = MutableLiveData()
    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val errorUsersLiveData: MutableLiveData<Boolean> = MutableLiveData()


    fun getUsers(db: DBHelper){
        loadingLiveData.value = true
        errorUsersLiveData.value = false

        viewModelScope.launch {

            try {

                mUsersLiveData.value = repository.getAllUsers(db = db)

            } catch (e : Exception){
                errorUsersLiveData.value = true
            }
            loadingLiveData.value = false

        }
    }
}