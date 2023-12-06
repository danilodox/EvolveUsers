package com.danilo.evolveusers.ui.userdetails

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilo.evolveusers.R
import com.danilo.evolveusers.data.database.DBHelper
import com.danilo.evolveusers.data.database.Result
import com.danilo.evolveusers.data.model.User
import com.danilo.evolveusers.ui.signup.FormState
import kotlinx.coroutines.launch

class UserDetailsViewModel (private val userDetailsRepository: UserDetailsRepository) : ViewModel(){

    val mUserDetailsLiveData = MutableLiveData<User>()
    private val toastMessage: LiveData<String>
        get() = _toastMessage

    private val _toastMessage = MutableLiveData<String>()

    private val _userDetailsForm = MutableLiveData<FormState>()
    val userDetailsFormState: LiveData<FormState> = _userDetailsForm

    private val _userDetailsResult = MutableLiveData<Result>()
    val userDetailsResult: LiveData<Result> = _userDetailsResult
    
    fun update(db: DBHelper, userModel: User, context: Context): Boolean {
        when (val result = userDetailsRepository.update(db, userModel)) {
            is Result.Success -> {
                handleSuccess(result.message)
                showToastMessage(context, result.message)
                return true
            }

            is Result.Failure -> {
                handleFailure(result.errorMessage)
                showToastMessage(context, result.errorMessage)
                return false
            }

        }
    }

    fun getUser(db: DBHelper, cpfCnpj: String): User? {

        viewModelScope.launch {
            try{
                this@UserDetailsViewModel.mUserDetailsLiveData.value = userDetailsRepository.getUser(db, cpfCnpj)
            } catch (e: Exception) {
                handleFailure(e.message.toString())
            }
        }


        return userDetailsRepository.getUser(db, cpfCnpj)
    }

    private fun handleFailure(errorMessage: String) {
        _toastMessage.value = errorMessage

    }

    private fun handleSuccess(message: String) {
        _toastMessage.value = message
    }

    private fun showToastMessage(context: Context, message: String) {
        _toastMessage.value = message
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    fun userDetailsDataChanged(email: String, password: String) {
        if (!isEmailValid(email)) {
            _userDetailsForm.value = FormState(emailError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            _userDetailsForm.value = FormState(passwordError = R.string.invalid_password)
        } else {
            _userDetailsForm.value = FormState(isDataValid = true)
        }
    }

    // A placeholder email validation check
    private fun isEmailValid(email: String): Boolean {
        return if (email.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            email.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}