package com.example.opitas.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.opitas.OpitasProtocol
import com.example.opitas.model.request.LibreTranslateRequest
import com.example.opitas.model.response.LibreTranslateResponse
import com.example.opitas.service.OpitasRepository
import com.example.opitas.service.OpitasResponse

class OpitasViewModel : ViewModel() {

    private lateinit var libreTranslateResponse: MutableLiveData<LibreTranslateResponse>
    private lateinit var opitasProtocol: OpitasProtocol

    fun setOpitasProtocol(protocol: OpitasProtocol){
        this.opitasProtocol = protocol
    }

    fun getTranslation(libreTranslateRequest: LibreTranslateRequest): LiveData<LibreTranslateResponse> {
        libreTranslateResponse = MutableLiveData()
        OpitasRepository().getTranslation(libreTranslateRequest, object :
            OpitasResponse<LibreTranslateResponse> {
            override fun onResponseErrorNotFound() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponseSuccess(response: LibreTranslateResponse) {
                libreTranslateResponse.value = response
            }

            override fun onResponseError(error: String) {
                opitasProtocol.responseError(error)
            }
        })
        return libreTranslateResponse
    }
}
