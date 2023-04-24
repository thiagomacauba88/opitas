package com.example.opitas.service

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class OpitasApplication: Application(), LifecycleObserver {

    companion object {
        lateinit var opitasApplication: OpitasApplication private set
        lateinit var opitasService: OpitasService private set
    }

    var BASE_URL = "https://libretranslate.com/"

    override fun onCreate() {
        super.onCreate()
        opitasApplication = this
        configureRetrofit()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    private fun configureRetrofit() {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(logging)
            .build()

        val gson = GsonBuilder().create()

        opitasService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(OpitasService::class.java)
    }
}
