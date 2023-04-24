package com.example.opitas.service

import com.example.opitas.R
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

abstract class BaseCallback<T> : Callback<T> {

    abstract fun onSuccess(response: T?)

    abstract fun onError(error: ErrorResponse)

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful) {
            onSuccess(response.body())
        } else {
            try {
                onError(
                    Gson().fromJson<ErrorResponse>(
                        Objects.requireNonNull<ResponseBody>(response.errorBody()).string(),
                        ErrorResponse::class.java
                    )
                )
            } catch (e: Exception) {
                val errorResponse = ErrorResponse()
                if(e.message!=null) {
                    errorResponse.message = e.message!!
                }
                else{
                    errorResponse.message = "It was not possible to connect"
                }
                onError(errorResponse)
            }
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        try{
            val response: ErrorResponse? = null
            if (t.message == null) {
                response!!.message = (OpitasApplication.opitasApplication.resources.getString(R.string.error_message))
                onError(response)
            } else if (t.message.equals("connect timed out", ignoreCase = true)) {
                response!!.message = (OpitasApplication.opitasApplication.resources.getString(R.string.error_message_timeout))
                onError(response)
            } else if (!t.message.equals("Socket closed", ignoreCase = true) && !t.message.equals(
                    "Canceled",
                    ignoreCase = true
                )
            ) {
                response!!.message = (OpitasApplication.opitasApplication.resources.getString(R.string.error_message))
                onError(response)
            }
        } catch (e: Exception) {
            val errorResponse = ErrorResponse()
            errorResponse.message = "It was not possible to connect"
            onError(errorResponse)
        }
    }
}
