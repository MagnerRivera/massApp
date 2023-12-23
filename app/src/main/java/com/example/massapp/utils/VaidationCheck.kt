package com.example.massapp.utils

import android.util.Patterns

fun validateDocument(document: String): RegisterValidation {
    if (document.isEmpty())
        return RegisterValidation.Failed("El documento no puede estar vacío")

    if (document.length < 10)
        return RegisterValidation.Failed("El documento debe tener más de 9 caracteres")

    return RegisterValidation.Success
}

fun validatePassword(password: String): RegisterValidation {
    if (password.isEmpty())
        return RegisterValidation.Failed("La contraseña  no puede estar vacía")

    if (password.length < 6)
        return RegisterValidation.Failed("La contraseña debe contener 6 caracteres")

    return RegisterValidation.Success
}

fun validateName(name: String): RegisterValidation {
    if (name.isEmpty())
        return RegisterValidation.Failed("El nombre  no puede estar vacío")

    return RegisterValidation.Success
}

fun validateAddress(address: String): RegisterValidation {
    if (address.isEmpty())
        return RegisterValidation.Failed("La direccion no puede estar vacio no puede estar vacío")

    return RegisterValidation.Success
}

fun validateEmail(email: String): RegisterValidation {
    if (email.isEmpty())
        return RegisterValidation.Failed("El correo electrónico no puede estar vacío")

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return RegisterValidation.Failed("Formato de correo electrónico incorrecto")

    return RegisterValidation.Success
}
