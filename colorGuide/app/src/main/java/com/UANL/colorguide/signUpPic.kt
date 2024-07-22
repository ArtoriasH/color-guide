package com.UANL.colorguide

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.UANL.colorguide.Models.Usuarios
import kotlinx.android.synthetic.main.activity_sign_up_pic.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*



class signUpPic : AppCompatActivity() {

    var name: String? = null
    var lastName: String? = null
    var email: String? = null
    var password: String? = null
    var imageUI: ImageView? =  null
    var imgArray:ByteArray? =  null

    //        Variables para escoger la foto
    lateinit var imageView: ImageView
    lateinit var button: Button
    private val pickImage = 100
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_pic)

        val extras = intent.extras
        if (extras != null) {
            name = extras.getString("name")
            lastName = extras.getString("lastName")
            email = extras.getString("email")
            password = extras.getString("password")
            //The key argument here must match that used in the other activity
        }

        imageView = findViewById(R.id.imageView2)
        button = findViewById(R.id.btnAvatar)
        btnAvatar.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        btnSignUp.setOnClickListener{
            registerUser()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
    }

    private fun registerUser() {
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val encodedString:String =  Base64.getEncoder().encodeToString(data)

        val strEncodeImage:String = "data:image/png;base64," + encodedString

        val Usuarios = Usuarios(0,
            name,
            lastName,
            email,
            password,
            strEncodeImage

        )
        val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<Int> = service.saveUser(Usuarios)

        result.enqueue(object: Callback<Int> {
            override fun onFailure(call: Call<Int>, t: Throwable) {
                Toast.makeText(this@signUpPic, t.message, Toast.LENGTH_LONG).show()

            }

            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                val item =  response.body()

                Toast.makeText(this@signUpPic,item.toString(), Toast.LENGTH_LONG).show()
            }
        })
        finish()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}