package com.UANL.colorguide

import android.app.Application
import com.UANL.colorguide.Data.DataDBHelper


class UserApplication: Application() {
//NOS VA PERMITIR QUE LO QUE ESTE AQUÍ DENTRO ACCEDER DESDE CUALQUIER PARTE

    companion object{
        //ESTAMOS DECLARANDO UNA VARIABLE QUE VAMOS A INSTANCIAR DESPUES
        lateinit var prefs:Prefs // ESTAMOS USANDO EL PATRON SINGLETON
        lateinit var dbHelper: DataDBHelper

    }

    override fun onCreate() {
        super.onCreate()
        //LE PASAMOS EL CONTEXTO GENERAL DE LA APLICACIÓN
        prefs = Prefs(applicationContext)
        dbHelper =  DataDBHelper(applicationContext)
    }
}