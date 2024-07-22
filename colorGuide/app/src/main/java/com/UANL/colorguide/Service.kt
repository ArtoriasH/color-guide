package com.UANL.colorguide

import com.UANL.colorguide.Models.Palette
import com.UANL.colorguide.Models.Reaction
import com.UANL.colorguide.Models.Usuarios
import retrofit2.Call
import retrofit2.http.*

//Retrofi usa una interface para hacer la petición hacia el servidor
interface Service {

    //Servicios para consumir el Album

    @GET("Usuario/usuarios")
    fun getUsuario(): Call<List<Usuarios>>

    // Este es mi select de Usuario por ID
    @GET("Usuario/usuarios/{id}")
    fun getUser(@Path("id") id: Int): Call<List<Usuarios>>

    @GET("Usuario/logIn/{mail}")
    fun logIn(@Path("mail") userMail: String):Call<List<Usuarios>>


    // Este es mi insert y update de Usuarios
    @Headers("Content-Type: application/json")
    @POST("Usuario/Save")
    fun saveUser(@Body userData: Usuarios):Call<Int>


    //-----Servicios para las paletas

    @GET("Palette/palette")
    fun getPalette(): Call<List<Palette>>

    @GET("Palette/recentPalette")
    fun getRecentPalette(): Call<List<Palette>>

    @GET("Palette/userPalette/{id}")
    fun getUserPalette(@Path("id") id: Int): Call<List<Palette>>

    @GET("Palette/getLastIdColor")
    fun getLastIdColor(): Call<List<Palette>>

    // Este es mi select de Palette por ID
    @GET("Palette/palette/{id}")
    fun getAPalette(@Path("id") id: Int): Call<List<Palette>>

    @Headers("Content-Type: application/json")
    @POST("Palette/Save")
    fun savePalette(@Body userData: Palette):Call<Int>

    //-----Servicios para reaccion

    @GET("Reaction/reaction")
    fun getReaction(): Call<List<Reaction>>

    // Este es mi insert y update de reaction
    @Headers("Content-Type: application/json")
    @POST("Reaction/Save")
    fun saveReaction(@Body userData: Reaction):Call<Int>



}