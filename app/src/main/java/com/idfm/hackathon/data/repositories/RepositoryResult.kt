package com.idfm.hackathon.data.repositories

import java.util.Date

sealed class RepositoryResult<T> {
    data class Success<T>(val data: T) : RepositoryResult<T>()
    data class Error<T>(val error: Throwable) : RepositoryResult<T>()
    data class Loading<T>(val at: Date = Date()) : RepositoryResult<T>()
}