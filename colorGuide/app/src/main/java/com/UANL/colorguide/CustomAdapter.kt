package com.UANL.colorguide

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.card_layout.*
import androidx.recyclerview.widget.RecyclerView
import com.UANL.colorguide.Models.Palette
import com.UANL.colorguide.Models.Usuarios
import com.squareup.picasso.Picasso
import java.util.Base64


class CustomAdapter(private val palettelist: ArrayList<Palette>): RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    var colorVibrant: String? = null

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position: Int)

    }

    fun setOnItemClickListener(listener : onItemClickListener){

        mListener = listener

    }




    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_layout, viewGroup, false)
        return ViewHolder(v, mListener)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentitem = palettelist[position]
        viewHolder.itemPaletteName.text = currentitem.nameP
        //viewHolder.itemImage.setImageResource(images[i])
        colorVibrant = currentitem.vibrant

        Picasso.get().load(currentitem.img).into(viewHolder.Image)
        var byteArray:ByteArray? = null
        val strImage:String = currentitem.img!!.replace("data:image/png;base64,","")
        byteArray =  Base64.getDecoder().decode(strImage)
        if(byteArray != null){
            viewHolder.Image!!.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))
        }
    }

    override fun getItemCount(): Int {
       // return Names.size
        return palettelist.size
    }


    inner class ViewHolder(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView){
        val Image: ImageView
        val itemPaletteName: TextView

        init {
            Image = itemView.findViewById(R.id.item_image)
            itemPaletteName = itemView.findViewById(R.id.item_paletteName)

            itemView.setOnClickListener {

                listener.onItemClick(adapterPosition)

            }
        }
    }
}