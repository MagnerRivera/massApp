package com.example.massapp.utils

sealed class Resource<T>(
    val data: T? = null,
    val menssage: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String) : Resource<T>(menssage = message)
    class Loading<T> : Resource<T>()
    class Unspecified<T> : Resource<T>()
}