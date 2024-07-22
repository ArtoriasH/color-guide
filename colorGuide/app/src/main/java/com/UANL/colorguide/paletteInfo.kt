package com.UANL.colorguide

import android.content.Intent
import android.graphics.Color
import android.graphics.Color.GREEN
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.UANL.colorguide.Models.Palette
import com.UANL.colorguide.Models.Usuarios
import kotlinx.android.synthetic.main.activity_palette_info.*
import kotlinx.android.synthetic.main.activity_profile.etLastName
import kotlinx.android.synthetic.main.activity_profile.etName
import kotlinx.android.synthetic.main.activity_profile.etPassword
import kotlinx.android.synthetic.main.activity_profile.imageProfile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Base64

class paletteInfo : AppCompatActivity() {

    //        Variables para escoger la foto
    lateinit var imageView: ImageView
    lateinit var button: Button
    private val pickImage = 100
    private var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_palette_info)
        imageView = findViewById(R.id.ivImageP)

        //Traemos la informacion del anterior activity
        val bundle : Bundle?= intent.extras
        val id = bundle!!.getInt("idPaleta")
        val nombreP = bundle!!.getString("nombreP")
        val vibrant = bundle!!.getString("vibrant")
        val muted = bundle!!.getString("muted")
        val darkmuted = bundle!!.getString("darkmuted")
        val darkvibrant = bundle!!.getString("darkvibrant")
        val dominant = bundle!!.getString("dominant")
        val creador = bundle!!.getInt("creador")

        val service:com.UANL.colorguide.Service = RestEngine.getRestEngine().create(
            com.UANL.colorguide.Service::class.java)
        val result: Call<List<Palette>> = service.getAPalette(id)
        Toast.makeText(this@paletteInfo, id.toString(), Toast.LENGTH_LONG).show()

        result.enqueue(object: Callback<List<Palette>> {

            override fun onFailure(call: Call<List<Palette>>, t: Throwable){
                Toast.makeText(this@paletteInfo,"Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Palette>>, response: Response<List<Palette>>){
                val item =  response.body()
                if (item != null && item.isNotEmpty()){
                    var byteArray:ByteArray? = null

                    val strImage:String =  item[0].img!!.replace("data:image/png;base64,","")
                    byteArray =  Base64.getDecoder().decode(strImage)
                    if(byteArray != null){

                        ivImageP!!.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))
                    }

                }else{

                }
            }
        })

        //Seteamos la info en pantallla
        tvnameP.text = nombreP

        var vibrantColor: Int? = null
        var mutedColor: Int? = null
        var darkmutedColor: Int? = null
        var darkvibrantColor: Int? = null
        var dominantColor: Int? = null

        if(vibrant != null){
             vibrantColor = Color.parseColor(vibrant)
             txtPVibrant!!.setBackgroundColor(vibrantColor!!)
             txtPVibrant.text = "Vibrant: " +vibrant
        }else{
            txtPVibrant!!.setBackgroundColor(0x00000000)
        }
        if(muted != null){
            mutedColor = Color.parseColor(muted)
            txtPMuted!!.setBackgroundColor(mutedColor!!)
            txtPMuted.text = "Muted: " +muted
        }else{
            txtPMuted!!.setBackgroundColor(0x00000000)
        }
        if(darkmuted != null){
            darkmutedColor = Color.parseColor(darkmuted)
            txtPDarkMuted!!.setBackgroundColor(darkmutedColor!!)
            txtPDarkMuted.text = "DarkMuted: " +darkmuted
        }else{
            txtPDarkMuted!!.setBackgroundColor(0x00000000)
        }
        if(darkvibrant != null){
            darkvibrantColor = Color.parseColor(darkvibrant)
            txtPDarkVibrant!!.setBackgroundColor(darkvibrantColor!!)
            txtPDarkVibrant.text = "DarkVibrant: " +darkvibrant
        }else{
            txtPDarkVibrant!!.setBackgroundColor(0x00000000)
        }
        if(dominant != null){
            dominantColor = Color.parseColor(dominant)
            txtPDominant!!.setBackgroundColor(dominantColor!!)
            txtPDominant.text = "Dominant: " +dominant
        }else{
            txtPDominant!!.setBackgroundColor(0x00000000)
        }
    }

}