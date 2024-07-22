package com.UANL.colorguide

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_palette.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.Base64
import androidx.palette.graphics.Palette

class createPalette : AppCompatActivity() {


    //        Variables para escoger la foto
    lateinit var imageView: ImageView
    lateinit var button: Button
    private val pickImage = 100
    private var imageUri: Uri? = null

    private var txtPVibrant: TextView? = null
    private var txtPMuted: TextView? = null
    private var txtPDominant: TextView? = null
    private var txtPDarkMuted: TextView? = null
    private var txtPDarkVibrant: TextView? = null

    var vibrant: String? = null
    var muted: String? = null
    var dominant: String? = null
    var darkmuted: String? = null
    var darkvibrant: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_palette)


        imageView = findViewById(R.id.ivImageUpload)
        val credential:Credential = UserApplication.prefs.getCredentials()
        val service:com.UANL.colorguide.Service = RestEngine.getRestEngine().create(
            com.UANL.colorguide.Service::class.java)

        Toast.makeText(this@createPalette, credential.id.toString(), Toast.LENGTH_LONG).show()




        btnUpload.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
            imageView.isDrawingCacheEnabled = true
            imageView.buildDrawingCache()
            txtPVibrant!!.text = "";
            txtPMuted!!.text = "";
            txtPDominant!!.text = "";
            txtPDarkMuted!!.text = "";
            txtPDarkVibrant!!.text = "";
        }

        btnSave.setOnClickListener {
            guardarPaleta()
        }

        btnSaveDraft.setOnClickListener{
            guardarBorrador()
        }

        txtPVibrant = findViewById(R.id.txtPVibrant)
        txtPMuted = findViewById(R.id.txtPMuted)
        txtPDominant = findViewById(R.id.txtPDominant)
        txtPDarkMuted = findViewById(R.id.txtPDarkMuted)
        txtPDarkVibrant = findViewById(R.id.txtPDarkVibrant)
    }

    private fun guardarBorrador() {
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val encodedString:String =  Base64.getEncoder().encodeToString(data)

        val strEncodeImage:String = "data:image/png;base64," + encodedString

        vibrant = txtPVibrant!!.getText().toString()
        muted = txtPMuted!!.getText().toString()
        dominant = txtPDominant!!.getText().toString()
        darkmuted = txtPDarkMuted!!.getText().toString()
        darkvibrant = txtPDarkVibrant!!.getText().toString()

        val credential:Credential = UserApplication.prefs.getCredentials()
        val service:com.UANL.colorguide.Service = RestEngine.getRestEngine().create(
            com.UANL.colorguide.Service::class.java)
        var paletteBorrador = com.UANL.colorguide.Models.Palette()


        paletteBorrador.nameP = etNamePalette.text.toString()
        paletteBorrador.vibrant =  vibrant
        paletteBorrador.muted = muted
        paletteBorrador.darkmuted =  dominant
        paletteBorrador.darkvibrant = darkmuted
        paletteBorrador.dominant = darkvibrant
        paletteBorrador.userId =  credential.id


        paletteBorrador.idPalette = 0
        paletteBorrador.img = encodedString
        UserApplication.dbHelper.insertPalette(paletteBorrador)
        //UserApplication.dbHelper.insertImg(img)

        Toast.makeText(this@createPalette, "Borrador guardado", Toast.LENGTH_LONG).show()
        val intent = Intent(this@createPalette, home::class.java)
        //intent.putExtra("idEvento", id)
        //intent.putExtra("isBorrador", true)
        startActivity(intent)

    }

    private fun guardarPaleta() {
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val encodedString:String =  Base64.getEncoder().encodeToString(data)

        val strEncodeImage:String = "data:image/png;base64," + encodedString
        val name = etNamePalette.text.toString()
        vibrant = txtPVibrant!!.getText().toString()
        muted = txtPMuted!!.getText().toString()
        dominant = txtPDominant!!.getText().toString()
        darkmuted = txtPDarkMuted!!.getText().toString()
        darkvibrant = txtPDarkVibrant!!.getText().toString()

        if(name.isNullOrEmpty()){
            showAlert()
            return
        }

       if(vibrant.isNullOrEmpty()){
           vibrant = null
       }
        if(muted.isNullOrEmpty()){
            muted = null
        }
        if(dominant.isNullOrEmpty()){
            dominant = null
        }
        if(darkmuted.isNullOrEmpty()){
            darkmuted = null
        }
        if(darkvibrant.isNullOrEmpty()){
            darkvibrant = null
        }

        val credential:Credential =  UserApplication.prefs.getCredentials()
        val palette = com.UANL.colorguide.Models.Palette(
            0,
            name,
            strEncodeImage,
            vibrant,
            muted,
            darkmuted,
            darkvibrant,
            dominant,
            credential.id
        )

        val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<Int> = service.savePalette(palette)

        result.enqueue(object: Callback<Int> {
            override fun onFailure(call: Call<Int>, t: Throwable) {
                Toast.makeText(this@createPalette, t.message, Toast.LENGTH_LONG).show()

            }

            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                val item =  response.body()

                Toast.makeText(this@createPalette,item.toString(), Toast.LENGTH_LONG).show()
            }
        })

        finish()
        val intent = Intent(this, home::class.java)
        startActivity(intent)


        ///
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error creando la paleta")
        builder.setPositiveButton("Aceptar", null)

        val dialog = builder.create()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            crearPalette(bitmap)
        }
    }

    private fun crearPalette(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            val vibrant = palette!!.vibrantSwatch
            if (vibrant != null) {
                txtPVibrant!!.setBackgroundColor(vibrant.rgb)
                txtPVibrant!!.setTextColor(vibrant.titleTextColor)
                // txt1!!.text = "Vibrant"
                txtPVibrant!!.text = "${vibrant?.rgb?.hexString()}"
               // txtvibrant = "Vibrant: ${vibrant?.rgb?.hexString()}"

            }else{
                txtPVibrant!!.setBackgroundColor(0x00000000)
            }

            val muted = palette!!.mutedSwatch

            if (muted != null) {
                txtPMuted!!.setBackgroundColor(muted.rgb)
                txtPMuted!!.setTextColor(muted.titleTextColor)
                //txt2!!.text = "Muted"
                txtPMuted!!.text = "${muted?.rgb?.hexString()}"
            }else{
                txtPMuted!!.setBackgroundColor(0x00000000)
            }


            val dominant = palette!!.dominantSwatch

            if (dominant != null) {
                txtPDominant!!.setBackgroundColor(dominant.rgb)
                txtPDominant!!.setTextColor(dominant.titleTextColor)
                //txt3!!.text = "Dominant"
                txtPDominant!!.text = "${dominant?.rgb?.hexString()}"
            }else{
                txtPDominant!!.setBackgroundColor(0x00000000)
            }

            val darkMuted = palette.darkMutedSwatch

            if (darkMuted != null) {
                txtPDarkMuted!!.setBackgroundColor(darkMuted.rgb)
                txtPDarkMuted!!.setTextColor(darkMuted.titleTextColor)
                //txt4!!.text = "Dark Muted"
                txtPDarkMuted!!.text = "${darkMuted?.rgb?.hexString()}"
            }
            else{
                txtPDarkMuted!!.setBackgroundColor(0x00000000)
            }

            val darkVibrant = palette.darkVibrantSwatch

            if (darkVibrant != null) {
                txtPDarkVibrant!!.setBackgroundColor(darkVibrant.rgb)
                txtPDarkVibrant!!.setTextColor(darkVibrant.titleTextColor)
                //txt5!!.text = "Dark Vibrant"
                txtPDarkVibrant!!.text = "${darkVibrant?.rgb?.hexString()}"

            }
            else{
                txtPDarkVibrant!!.setBackgroundColor(0x00000000)
            }

        }
    }

    fun Int.hexString():String{
        return String.format("#%06X", (0xFFFFFF and  this))
    }


}