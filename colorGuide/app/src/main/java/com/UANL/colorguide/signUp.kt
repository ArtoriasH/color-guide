package com.UANL.colorguide

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

import kotlinx.android.synthetic.main.activity_sign_up.*



class signUp : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val btnSignUp = findViewById<Button>(R.id.btnSignUp)


        btnSignUp.setOnClickListener {
            crearUser()
        }
    }

    private fun crearUser(){
        val name = etName.text.toString()
        val lastName = etLastName.text.toString()
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()


        if(name.isNullOrEmpty()||
            lastName.isNullOrEmpty()||
            email.isNullOrEmpty()||
            password.isNullOrEmpty()){
            showAlert()
            return
        }

        val intent = Intent(this, signUpPic::class.java)
        intent.putExtra("name",name);
        intent.putExtra("lastName",lastName);
        intent.putExtra("email",email);
        intent.putExtra("password",password);
        startActivity(intent)


    }


    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error creando al usuario")
        builder.setPositiveButton("Aceptar", null)

        val dialog = builder.create()
        dialog.show()
    }
}