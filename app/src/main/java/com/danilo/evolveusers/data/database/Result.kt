package com.danilo.evolveusers.data.database

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result {
    data class Success(val message: String) : Result()
    data class Failure(val errorMessage: String) : Result()
}
