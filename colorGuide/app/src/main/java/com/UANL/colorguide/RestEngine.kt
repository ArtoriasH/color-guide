package com.UANL.colorguide

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestEngine {

    // nos permite acceder sin instanciar el objecto
    companion object{
        fun getRestEngine(): Retrofit {
            //Creamos el interceptor
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client =  OkHttpClient.Builder().addInterceptor(interceptor).build()
            val retrofit =  Retrofit.Builder()
                .baseUrl("https://palettepsm1.000webhostapp.com/PCApi.api/") // tu url
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return  retrofit

        }
    }
}