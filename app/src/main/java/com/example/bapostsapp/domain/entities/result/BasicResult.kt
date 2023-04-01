package com.example.bapostsapp.domain.entities.result

/**
 * A basic result class meant to be returned and indicate a successful or failed operation with
 * optional parameters for message (in case of [BasicResult.Success]) and exception (in case of
 * [BasicResult.Failure]).
 */
sealed class BasicResult{
    data class Success(val message: String?): BasicResult()
    data class Failure(val exception: Throwable?): BasicResult()
}