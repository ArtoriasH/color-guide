package com.UANL.colorguide

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.UANL.colorguide.Models.Palette
import com.UANL.colorguide.Models.Reaction
import kotlinx.android.synthetic.main.activity_pallete_fav.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Base64

class palleteFav : AppCompatActivity() {

    //        Variables para escoger la foto
    lateinit var imageView: ImageView
    lateinit var button: Button
    private val pickImage = 100
    private var imageUri: Uri? = null
    private var idP: Int? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pallete_fav)

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


        idP = id



        val service: Service = RestEngine.getRestEngine().create(
            Service::class.java)
        val result: Call<List<Palette>> = service.getAPalette(id)
        Toast.makeText(this@palleteFav, id.toString(), Toast.LENGTH_LONG).show()

        result.enqueue(object: Callback<List<Palette>> {

            override fun onFailure(call: Call<List<Palette>>, t: Throwable){
                Toast.makeText(this@palleteFav,"Error", Toast.LENGTH_LONG).show()
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

        btnSaveFavorite.setOnClickListener{
            addFav()
        }
    }


    private fun addFav() {
        val credential:Credential =  UserApplication.prefs.getCredentials()
        val reaction = Reaction(0,
            credential.id,
            idP
        )

        val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<Int> = service.saveReaction(reaction)

        result.enqueue(object: Callback<Int> {
            override fun onFailure(call: Call<Int>, t: Throwable) {
                Toast.makeText(this@palleteFav, t.message, Toast.LENGTH_LONG).show()

            }

            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                val item =  response.body()

                Toast.makeText(this@palleteFav,item.toString(), Toast.LENGTH_LONG).show()
            }
        })

        finish()
        val intent = Intent(this, home::class.java)
        startActivity(intent)
    }


}
