package com.example.massapp.utils

// Clase sellada (sealed class) que representa el resultado de la validación del registro
sealed class RegisterValidation {
    // Objeto para representar una validación exitosa
    object Success : RegisterValidation()

    // Clase de datos para representar una validación fallida con un mensaje asociado
    data class Failed(val message: String) : RegisterValidation()
}

// Modelo de datos que representa el estado de los campos de registro
data class RegisterFieldsState(
    val document: RegisterValidation,  // Estado de validación para el campo del documento
    val password: RegisterValidation,  // Estado de validación para el campo de la contraseña
    val username: RegisterValidation,  // Estado de validación para el campo del nombre de usuario
    val address: RegisterValidation,   // Estado de validación para el campo de la dirección
    val email: RegisterValidation      // Estado de validación para el campo del correo electrónico
)