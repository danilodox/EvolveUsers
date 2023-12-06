package com.danilo.evolveusers.ui.signup

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
import com.danilo.evolveusers.data.network.repository.UserRepository
import kotlinx.coroutines.launch

class SignupViewModel(private val signupRepository: SignupRepository, private val userRepository: UserRepository) : ViewModel(){

    // MutableLiveData para observar mensagens de feedback na UI
    private val toastMessage: LiveData<String>
        get() = _toastMessage

    private val _toastMessage = MutableLiveData<String>()

    private val _signupForm = MutableLiveData<FormState>()
    val signupFormState: LiveData<FormState> = _signupForm

    private val _signupResult = MutableLiveData<Result>()
    val signupResult: LiveData<Result> = _signupResult

     fun signUp(db: DBHelper, userModel: User, context: Context, onComplete: (Boolean) -> Unit) {
         viewModelScope.launch{
                try{
                    when (val result = signupRepository.signup(db, userModel)) {
                        is Result.Success -> {
                            handleSuccess(result.message)
                            showToastMessage(context, result.message)

                            sendUserPost(userModel)
                            onComplete(true)

                        }

                        is Result.Failure -> {
                            handleFailure(result.errorMessage)
                            showToastMessage(context, result.errorMessage)
                            onComplete(false)
                        }

                    }
                } catch (e: Exception) {
                    handleFailure(e.message.toString())
                    showToastMessage(context, e.message.toString())
                    onComplete(false)
                }
         }
     }

    private suspend fun sendUserPost(user: User) {

        userRepository.postUser(user)
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
    fun signupDataChanged(email: String, password: String) {
        if (!isEmailValid(email)) {
            _signupForm.value = FormState(emailError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            _signupForm.value = FormState(passwordError = R.string.invalid_password)
        } else {
            _signupForm.value = FormState(isDataValid = true)
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