package com.UANL.colorguide

import android.content.Context

class Prefs(val context: Context) {

    //Constantes

    val SHARED_NAME = "USERPREFERENCES" // Poner el nombre que mejor convenga

    // Manejador shared preferences

    val managerPrefs = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)


    // Funcion para guardar credenciales
    fun saveCredentials(credential: Credential){
        var editor = managerPrefs.edit()
        editor.putString("user", credential.strUser)
        editor.putString("password", credential.strPassword)
        editor.putInt("id", credential.id)
        editor.putString("name", credential.name)
        editor.putString("lastName", credential.lastName)
        editor.commit()
    }

    fun getCredentials():Credential{

        val credential:Credential =  Credential()
        //ENCASO DE QUE NO HAYA DATOS REGRESA UN VALOR POR DEFAULT
        val strUser: String? =  managerPrefs.getString("user","No information exists")
        val strPassword:String? =  managerPrefs.getString("password", "No information exists")
        val id:Int? =  managerPrefs.getInt("id", -1)
        val strName:String? =  managerPrefs.getString("name", "No information exists")
        val strlastName:String? =  managerPrefs.getString("lastName", "No information exists")


        credential.strUser =  strUser!!
        credential.strPassword =  strPassword!!
        credential.id =  id!!
        credential.name =  strName!!
        credential.lastName =  strlastName!!


        return credential
    }
}