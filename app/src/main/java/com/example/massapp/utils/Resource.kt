package com.example.massapp.utils

// Clase sellada (sealed class) que representa un recurso con diferentes estados
sealed class Resource<T>(
    val data: T? = null,          // Datos asociados al recurso (pueden ser nulos)
    val message: String? = null   // Mensaje asociado al recurso (puede ser nulo)
) {
    // Clase interna para representar un recurso exitoso con datos asociados
    class Success<T>(data: T) : Resource<T>(data)

    // Clase interna para representar un recurso con error y un mensaje asociado
    class Error<T>(message: String) : Resource<T>(message = message)

    // Clase interna para representar un recurso en estado de carga
    class Loading<T> : Resource<T>()

    // Clase interna para representar un recurso no especificado
    class Unspecified<T> : Resource<T>()
}