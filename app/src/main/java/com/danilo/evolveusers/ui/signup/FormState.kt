package com.danilo.evolveusers.ui.signup

/**
 * Data validation state of the register form.
 */
data class FormState (

    val emailError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)