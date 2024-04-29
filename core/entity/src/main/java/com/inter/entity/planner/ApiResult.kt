package com.inter.entity.planner


sealed class ApiResult<out T> {
    data class Loading(val data: Boolean) : ApiResult<Nothing>()
    data class Error<T>(val data: T) : ApiResult<Nothing>()
    data class Success<T>(val data: T) : ApiResult<T>()
}