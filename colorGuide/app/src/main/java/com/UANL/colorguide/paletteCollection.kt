package com.UANL.colorguide

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.UANL.colorguide.Models.Palette
import com.UANL.colorguide.Models.Usuarios
import kotlinx.android.synthetic.main.activity_palette_collection.searchFilter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class paletteCollection : AppCompatActivity() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var paletteArrayList : ArrayList<Palette>
    private lateinit var tempArrayList : ArrayList<Palette>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_palette_collection)


        val credential:Credential = UserApplication.prefs.getCredentials()
        val service:com.UANL.colorguide.Service = RestEngine.getRestEngine().create(
            com.UANL.colorguide.Service::class.java)
        val result: Call<List<Usuarios>> = service.getUser(credential.id)
        Toast.makeText(this@paletteCollection, credential.id.toString(), Toast.LENGTH_LONG).show()



        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        paletteArrayList = arrayListOf<Palette>()
        tempArrayList = arrayListOf<Palette>()
        getusuarioData()

        val searchViewInput =  searchFilter as SearchView
        searchViewInput.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                tempArrayList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if(searchText.isNotEmpty()){

                    paletteArrayList.forEach{
                        if(it.nameP?.lowercase(Locale.getDefault())?.contains(searchText) == true){

                            tempArrayList.add(it)
                        }else if(it.vibrant?.contains(searchText) == true){
                            tempArrayList.add(it)
                        }
                    }
                    recyclerView.adapter?.notifyDataSetChanged()

                }else{

                    tempArrayList.clear()
                    tempArrayList.addAll(paletteArrayList)
                    recyclerView.adapter!!.notifyDataSetChanged()

                }
                return false
            }
        })
    }

    private fun getusuarioData() {
        val credential:Credential = UserApplication.prefs.getCredentials()
        val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<List<Palette>> = service.getUserPalette(credential.id)

        result.enqueue(object: Callback<List<Palette>> {

            override fun onFailure(call: Call<List<Palette>>, t: Throwable){
                Toast.makeText(this@paletteCollection,"Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Palette>>, response: Response<List<Palette>>){
                val arrayItems =  response.body()
                if (arrayItems != null){
                    paletteArrayList.clear()
                    for (item in arrayItems!!){
                        paletteArrayList.add(item!!)

                    }

                    tempArrayList.addAll(paletteArrayList)

                    var adapter = CustomAdapter(tempArrayList)
                    recyclerView.adapter = adapter
                    adapter.setOnItemClickListener(object : CustomAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@paletteCollection, paletteInfo::class.java)
                            intent.putExtra("idPaleta", tempArrayList[position].idPalette)
                            intent.putExtra("nombreP", tempArrayList[position].nameP)
                            intent.putExtra("vibrant", tempArrayList[position].vibrant)
                            intent.putExtra("muted", tempArrayList[position].muted)
                            intent.putExtra("darkmuted", tempArrayList[position].darkmuted)
                            intent.putExtra("darkvibrant", tempArrayList[position].darkvibrant)
                            intent.putExtra("dominant", tempArrayList[position].dominant)
                            intent.putExtra("creador", tempArrayList[position].userId)
                            startActivity(intent)
                        }
                    })
                }
                recyclerView?.visibility = View.VISIBLE
            }
        })

    }
    }
