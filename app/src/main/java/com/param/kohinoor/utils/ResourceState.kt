package com.param.exercise.utils

sealed class ResourceState <out T> {
    object Loading : ResourceState<Nothing>()
    object Idle : ResourceState<Nothing>()
    data class Error(val throwable: Throwable) : ResourceState<Nothing>()
    data class Success<T>(val item: T) : ResourceState<T>()
}