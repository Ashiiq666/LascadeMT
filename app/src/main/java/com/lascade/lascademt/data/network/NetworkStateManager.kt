package com.lascade.lascademt.data.network

sealed class NetworkStateManager<T>(
    val data: T? = null,
    val message: String? = null,
    val throwable: Throwable? = null
) {
    class Success<T>(data: T) : NetworkStateManager<T>(data)
    class Error<T>(message: String, data: T? = null, throwable: Throwable? = null) :
        NetworkStateManager<T>(data, message, throwable)
}