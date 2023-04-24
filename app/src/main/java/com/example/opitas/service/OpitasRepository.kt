package com.example.opitas.service

import com.example.opitas.model.request.LibreTranslateRequest
import com.example.opitas.model.response.LibreTranslateResponse
import retrofit2.Call

class OpitasRepository() {

    private val opitasService = OpitasApplication.opitasService

    fun getTranslation(libreTranslateRequest: LibreTranslateRequest, listener: OpitasResponse<LibreTranslateResponse>) {
        opitasService.getTranslation(libreTranslateRequest).enqueue(object : BaseCallback<LibreTranslateResponse>() {
            override fun onError(error: ErrorResponse) {
                error.message?.let { listener.onResponseError(it) }
            }

            override fun onSuccess(response: LibreTranslateResponse?) {
                response?.let { listener.onResponseSuccess(it) }
            }

            override fun onFailure(call: Call<LibreTranslateResponse>, t: Throwable) {
                listener.onResponseError("It was not possible to connect")
            }
        })
    }
}
