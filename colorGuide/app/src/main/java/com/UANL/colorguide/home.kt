package com.UANL.colorguide

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.UANL.colorguide.Models.Palette
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_palette_collection.searchFilter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var recyclerView : RecyclerView
    private lateinit var paletteArrayList : ArrayList<Palette>
    private lateinit var tempArrayList : ArrayList<Palette>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

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
        val result: Call<List<Palette>> = service.getRecentPalette()

        result.enqueue(object: Callback<List<Palette>> {

            override fun onFailure(call: Call<List<Palette>>, t: Throwable){
                Toast.makeText(this@home,"Error", Toast.LENGTH_LONG).show()
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
                            val intent = Intent(this@home, palleteFav::class.java)
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.nav_item_one -> startActivity(Intent(this, createPalette::class.java))
            R.id.nav_item_two -> startActivity(Intent(this, paletteCollection::class.java))
            R.id.nav_item_four -> startActivity(Intent(this, paletteFavCollection::class.java))
            R.id.nav_item_six -> startActivity(Intent(this, profile::class.java))
            R.id.nav_item_seven -> startActivity(Intent(this, MainActivity::class.java))
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}