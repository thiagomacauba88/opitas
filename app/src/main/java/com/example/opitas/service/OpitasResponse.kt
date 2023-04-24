package com.example.opitas.service

interface OpitasResponse<T> {
    fun onResponseSuccess(response: T)
    fun onResponseError(message: String)
    fun onResponseErrorNotFound()
}
