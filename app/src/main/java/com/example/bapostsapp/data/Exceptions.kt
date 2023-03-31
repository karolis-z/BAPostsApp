package com.example.bapostsapp.data

sealed class ApiException(message: String?, throwable: Throwable?) : Exception() {

    class NoConnectionException(message: String?, throwable: Throwable?) :
        ApiException(message, throwable)

    class FailedApiRequestException(message: String?, throwable: Throwable?) :
        ApiException(message, throwable)

}