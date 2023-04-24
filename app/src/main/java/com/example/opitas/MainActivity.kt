package com.example.opitas

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.opitas.viewModel.OpitasViewModel
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.opitas.model.request.LibreTranslateRequest
import com.example.opitas.model.response.LibreTranslateResponse

class MainActivity : FragmentActivity(), OpitasProtocol, AdapterView.OnItemSelectedListener {

    lateinit var viewModel: OpitasViewModel
    var languages = arrayOf("pt", "es")
    var languageSelected = "pt"
    lateinit var spinner: Spinner
    lateinit var translateFromTextView: TextView
    lateinit var editTextTextFrom: EditText
    lateinit var editTextTextTo: EditText
    lateinit var translateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        viewModel = ViewModelProvider(this)[OpitasViewModel::class.java]
        viewModel.setOpitasProtocol(this)
        initSpinner()
        initListeners()
    }

    private fun initViews() {
        translateFromTextView = findViewById(R.id.translateFromTextView)
        spinner = findViewById(R.id.spinner)
        translateButton = findViewById(R.id.translateButton)
        editTextTextFrom = findViewById(R.id.editTextTextFrom)
        editTextTextTo = findViewById(R.id.editTextTextTo)
    }

    private fun initListeners() {
        translateButton.setOnClickListener {
            if (editTextTextFrom.text.isEmpty()) {
                Toast.makeText(this, "Tap a text and try again", Toast.LENGTH_SHORT).show()
            } else {
                val libreTranslateRequest = LibreTranslateRequest()
                libreTranslateRequest.api_key = this.resources.getString(R.string.key)
                libreTranslateRequest.q = editTextTextFrom.text.toString()
                libreTranslateRequest.source = spinner.selectedItem.toString()
                libreTranslateRequest.format = "text"
                libreTranslateRequest.target = "en"

                viewModel.getTranslation(libreTranslateRequest)
                    .observe(this, Observer<LibreTranslateResponse> { response ->
                        editTextTextTo.setText(response.translatedText)
                    })
            }
        }
    }

    private fun initSpinner() {
        spinner!!.setOnItemSelectedListener(this)
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.setAdapter(aa)
    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
        translateFromTextView.setText("Translate from: ")
        editTextTextFrom.setText("")
        editTextTextTo.setText("")
        languageSelected = languages[position]
        translateFromTextView.append(languageSelected)
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {}

    override fun responseError(strError: String) {
        Toast.makeText(this, strError, Toast.LENGTH_SHORT).show()
    }

    override fun responseErrorTrip(strError: String) {
        Toast.makeText(this, strError, Toast.LENGTH_SHORT).show()
    }
}

interface OpitasProtocol {
    fun responseError(strError: String)
    fun responseErrorTrip(strError: String)
}
