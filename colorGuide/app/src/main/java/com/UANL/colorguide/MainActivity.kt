package com.UANL.colorguide

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.UANL.colorguide.Models.Usuarios
import com.UANL.colorguide.UserApplication.Companion.prefs
import com.UANL.colorguide.factory.DialogFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_up.etPassword
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        // Ejemplo de uso: obtener todos los usuarios de la base de datos
        CoroutineScope(Dispatchers.IO)


        val credential:Credential =  UserApplication.prefs.getCredentials()
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        val service:com.UANL.colorguide.Service = RestEngine.getRestEngine().create(
            com.UANL.colorguide.Service::class.java)


        btnSignUp.setOnClickListener {
            val intent = Intent(this, signUp::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            login()
        }

    }

    // Datos de Usuario
    private var nombre: String = ""
    private var apellido: String = ""
    private var password: String = ""

    private fun login() {
        val mail = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if(mail.isNullOrEmpty()||password.isNullOrEmpty()){
            val appDialogo = DialogFactory.Application.getApplicationDocument(DialogFactory.DocumentType.EmptyLoginData).crearDialogo()
            appDialogo.mostrarDialogo(this@MainActivity)
            return
        }

        val service: com.UANL.colorguide.Service =  RestEngine.getRestEngine().create(com.UANL.colorguide.Service::class.java)
        val result: Call<List<Usuarios>> = service.logIn(mail)



        result.enqueue(object: Callback<List<Usuarios>> {

            override fun onFailure(call: Call<List<Usuarios>>, t: Throwable){
                Toast.makeText(this@MainActivity,t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Usuarios>>, response: Response<List<Usuarios>>){
                val item =  response.body()
                if (item != null && item.isNotEmpty()){

                    if(password == item[0]?.password){

                        val credential: Credential = Credential()
                        credential.strUser = item[0]?.mail!!
                        credential.strPassword = item[0]?.password!!
                        credential.id = item[0]?.idUser!!
                        credential.name = item[0]?.name!!
                        credential.lastName = item[0]?.lastName!!

                        //Guardamos
                        prefs.saveCredentials(credential)


                        val intent = Intent(this@MainActivity, home::class.java)

                        startActivity(intent)
                    }else{
                        val appDialogo = DialogFactory.Application.getApplicationDocument(DialogFactory.DocumentType.PasswordError).crearDialogo()
                        appDialogo.mostrarDialogo(this@MainActivity)
                    }
                }else{
                    val appDialogo = DialogFactory.Application.getApplicationDocument(DialogFactory.DocumentType.ErrorFoundUser).crearDialogo()
                    appDialogo.mostrarDialogo(this@MainActivity)
                }
            }
        })
    }

}