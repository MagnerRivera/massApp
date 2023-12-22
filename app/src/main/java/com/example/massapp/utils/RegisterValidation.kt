package com.example.massapp.utils

sealed class RegisterValidation {
    object Success : RegisterValidation()
    data class Failed(val message: String) : RegisterValidation()
}

data class RegisterFieldsState(
    val document: RegisterValidation,
    val password: RegisterValidation,
    val username: RegisterValidation,
    val address: RegisterValidation,
    val email: RegisterValidation
)