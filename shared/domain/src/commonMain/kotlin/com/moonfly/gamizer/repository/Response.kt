package com.moonfly.gamizer.repository

sealed class Response<out T> {

    data class Success<T>(val body: T) : Response<T>()

    sealed class Error : Response<Nothing>() {

        data class HttpError(val code: Int, val errorBody: String) : Error()

        data object NetworkError : Error()

        data object SerializationError : Error()
    }
}

fun <T, R> Response<T>.map(mapper: (T) -> R): Response<R> {
    return when (this) {
        is Response.Success -> Response.Success(mapper(body))
        is Response.Error -> this
    }
}