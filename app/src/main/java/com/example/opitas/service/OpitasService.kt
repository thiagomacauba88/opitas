package com.example.opitas.service

import com.example.opitas.model.request.LibreTranslateRequest
import com.example.opitas.model.response.LibreTranslateResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface OpitasService {

    @POST("translate")
    fun getTranslation(@Body saveLatLongByTripRequest: LibreTranslateRequest): Call<LibreTranslateResponse>
}
