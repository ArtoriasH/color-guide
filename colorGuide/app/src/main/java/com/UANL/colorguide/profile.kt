package com.UANL.colorguide

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.UANL.colorguide.Models.Usuarios
import com.UANL.colorguide.factory.DialogFactory
import kotlinx.android.synthetic.main.activity_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.Base64

class profile : AppCompatActivity(){

    //        Variables para escoger la foto
    lateinit var imageView: ImageView
    lateinit var button: Button
    private val pickImage = 100
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        imageView = findViewById(R.id.imageProfile)

        val credential:Credential = UserApplication.prefs.getCredentials()
        val service:com.UANL.colorguide.Service = RestEngine.getRestEngine().create(
            com.UANL.colorguide.Service::class.java)
        val result: Call<List<Usuarios>> = service.getUser(credential.id)
        Toast.makeText(this@profile, credential.id.toString(), Toast.LENGTH_LONG).show()

        result.enqueue(object: Callback<List<Usuarios>> {

            override fun onFailure(call: Call<List<Usuarios>>, t: Throwable){
                Toast.makeText(this@profile,"Error", Toast.LENGTH_LONG).show()
            }



            override fun onResponse(call: Call<List<Usuarios>>, response: Response<List<Usuarios>>){


                val item =  response.body()
                if (item != null && item.isNotEmpty()){

                    etName.setText(item[0].name)
                    etLastName.setText(item[0].lastName)
                    etPassword.setText(item[0].password)
                    tvfullName.setText(item[0].name + " " +item[0].lastName)

                    var byteArray:ByteArray? = null

                    val strImage:String =  item[0].avatar!!.replace("data:image/png;base64,","")
                    byteArray =  Base64.getDecoder().decode(strImage)
                    if(byteArray != null){
                        imageProfile!!.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))
                    }

                }else{

                }
            }
        })

        btnUpdateProfile.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setMessage("¿Estas seguro de querer editar tus datos?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->

                    updateUsuario()
                }
                .setNegativeButton("No") { dialog, id ->

                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()


        }

        btnBorradores.setOnClickListener{
            val intent = Intent(this, BorradoresActivity::class.java)
            startActivity(intent)
        }

        btnAvatar.setOnClickListener{
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
    }

    private fun updateUsuario() {
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val encodedString:String =  Base64.getEncoder().encodeToString(data)

        val strEncodeImage:String = "data:image/png;base64," + encodedString

        val credential:Credential =  UserApplication.prefs.getCredentials()
        val usuarios = Usuarios(credential.id,
            etName.text.toString(),
            etLastName.text.toString(),
            credential.strUser,
            etPassword.text.toString(),
            strEncodeImage
        )

        val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<Int> = service.saveUser(usuarios)

        result.enqueue(object: Callback<Int> {
            override fun onFailure(call: Call<Int>, t: Throwable) {
                Toast.makeText(this@profile, t.message, Toast.LENGTH_LONG).show()

            }

            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                //val item =  response.body()
                Toast.makeText(this@profile, "Usuario editado", Toast.LENGTH_LONG).show()
            }
        })

        finish()
        val intent = Intent(this, home::class.java)
        startActivity(intent)

    }


}




