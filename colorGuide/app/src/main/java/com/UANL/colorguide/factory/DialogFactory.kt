package com.UANL.colorguide.factory

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import org.w3c.dom.DocumentType

class DialogFactory: Application() {

    interface Dialogo{
        fun mostrarDialogo(context: Context)
    }

    class ErrorDoc : Dialogo{
        override fun mostrarDialogo(context: Context){
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Error")
            builder.setMessage("Hay un error al realizar la operacion")
            builder.setPositiveButton("Aceptar", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    class ErrorPassword : Dialogo{
        override fun mostrarDialogo(context: Context){
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Error")
            builder.setMessage("Contraseña incorrecta")
            builder.setPositiveButton("Aceptar", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    class ErrorFUser : Dialogo{
        override fun mostrarDialogo(context: Context){
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Error")
            builder.setMessage("Usuario no encontrado")
            builder.setPositiveButton("Aceptar", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    class EmptyLData : Dialogo{
        override fun mostrarDialogo(context: Context){
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Error")
            builder.setMessage("Asegurate de ingresar los datos necesarios")
            builder.setPositiveButton("Aceptar", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    class SuccessDoc: Dialogo{
        override fun mostrarDialogo(context: Context){
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Exito")
            builder.setMessage("Los cambios fueron realizados")
            builder.setPositiveButton("Aceptar", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    class SinConexionDoc : Dialogo{
        override fun mostrarDialogo(context: Context){
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Error de red")
            builder.setMessage("No se cuenta con conexion a internet")
            builder.setPositiveButton("Aceptar", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }
    abstract class Application{
        abstract fun crearDialogo(): Dialogo

        companion object{
            fun getApplicationDocument(documentType: DocumentType): Application{
                return when (documentType){
                    DocumentType.Success -> Opciones()
                    DocumentType.Error -> AnError()
                    DocumentType.PasswordError -> APasswordError()
                    DocumentType.ErrorFoundUser -> ErrorFoundUser()
                    DocumentType.EmptyLoginData -> EmptyLoginData()
                    DocumentType.Internet -> Internet()
                    else -> throw Exception("Invalid document type")
                }
            }
        }
    }

    class Opciones : Application() {
        override fun crearDialogo() = SuccessDoc()
    }

    class AnError: Application() {
        override fun crearDialogo() = ErrorDoc()
    }

    class APasswordError: Application() {
        override fun crearDialogo() = ErrorPassword()
    }

    class ErrorFoundUser: Application() {
        override fun crearDialogo() = ErrorFUser()
    }

    class EmptyLoginData: Application() {
        override fun crearDialogo() = EmptyLData()
    }

    class Internet: Application() {
        override fun crearDialogo() = SinConexionDoc()
    }

    enum class DocumentType {Success, PasswordError, ErrorFoundUser, EmptyLoginData, Error, Internet}

}