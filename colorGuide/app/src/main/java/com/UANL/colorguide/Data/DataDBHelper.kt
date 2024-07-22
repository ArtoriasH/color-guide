package com.UANL.colorguide.Data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.UANL.colorguide.Models.Palette
import java.lang.Exception

class DataDBHelper (var context: Context): SQLiteOpenHelper(context,SetDB.DB_NAME,null,SetDB.DB_VERSION){

    override fun onCreate(db: SQLiteDatabase?){
        //SI NO EXISTE LA BASE DE DATOS LA CREA
        try{

            val createEventTable:String =  "CREATE TABLE " + SetDB.tblPalette.TABLE_NAME + "(" +
                    SetDB.tblPalette.COL_IDPALETTE + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SetDB.tblPalette.COL_NAMEP + " VARCHAR(20)," +
                    SetDB.tblPalette.COL_VIBRANT + " VARCHAR(15)," +
                    SetDB.tblPalette.COL_MUTED + " VARCHAR(15)," +
                    SetDB.tblPalette.COL_DARKMUTED + " VARCHAR(15)," +
                    SetDB.tblPalette.COL_IMG + " BLOB,"+
                    SetDB.tblPalette.COL_DARKVIBRANT + " VARCHAR(15)," +
                    SetDB.tblPalette.COL_DOMINANT + " VARCHAR(15)," +
                    SetDB.tblPalette.COL_USERID + " INTEGER)"
            db?.execSQL(createEventTable)

            Log.e("ENTRO","CREO TABLAS")

        }catch (e: Exception){
            Log.e("Execption", e.toString())
        }

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    public fun insertPalette(palette: Palette):Boolean{

        val dataBase:SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult:Boolean =  true

        values.put(SetDB.tblPalette.COL_NAMEP,palette.nameP)
        values.put(SetDB.tblPalette.COL_VIBRANT,palette.vibrant)
        values.put(SetDB.tblPalette.COL_MUTED,palette.muted)
        values.put(SetDB.tblPalette.COL_DARKMUTED,palette.darkmuted)
        values.put(SetDB.tblPalette.COL_IMG,palette.img)
        values.put(SetDB.tblPalette.COL_DARKVIBRANT,palette.darkvibrant)
        values.put(SetDB.tblPalette.COL_DOMINANT,palette.dominant)
        values.put(SetDB.tblPalette.COL_USERID,palette.userId)

        try {
            val result =  dataBase.insert(SetDB.tblPalette.TABLE_NAME, null, values)

            if (result == (0).toLong()) {
                Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
            }

        }catch (e: Exception){
            Log.e("Execption", e.toString())
            boolResult =  false
        }

        dataBase.close()

        return boolResult
    }

    @SuppressLint("Range")
    public fun getListOfPalette():MutableList<Palette> {
        val List: MutableList<Palette> = ArrayList()

        val dataBase: SQLiteDatabase = this.writableDatabase

        //QUE COLUMNAS QUEREMOS QUE ESTE EN EL SELECT
        val columns: Array<String> = arrayOf(
            SetDB.tblPalette.COL_IDPALETTE,
            SetDB.tblPalette.COL_NAMEP,
            SetDB.tblPalette.COL_VIBRANT,
            SetDB.tblPalette.COL_MUTED,
            SetDB.tblPalette.COL_DARKMUTED,
            SetDB.tblPalette.COL_IMG,
            SetDB.tblPalette.COL_DARKVIBRANT,
            SetDB.tblPalette.COL_DOMINANT,
            SetDB.tblPalette.COL_USERID
        )

        val data = dataBase.query(
            SetDB.tblPalette.TABLE_NAME,
            columns,
            null,
            null,
            null,
            null,
            SetDB.tblPalette.COL_IDPALETTE + " ASC"
        )

        // SI NO TIENE DATOS DEVUELVE FALSO
        //SE MUEVE A LA PRIMERA POSICION DE LOS DATOS
        if (data.moveToFirst()) {

            do {
                val palette = Palette()
                palette.idPalette = data.getString(data.getColumnIndex(SetDB.tblPalette.COL_IDPALETTE)).toInt()
                palette.nameP = data.getString(data.getColumnIndex(SetDB.tblPalette.COL_NAMEP)).toString()
                palette.vibrant = data.getString(data.getColumnIndex(SetDB.tblPalette.COL_VIBRANT)).toString()
                palette.muted = data.getString(data.getColumnIndex(SetDB.tblPalette.COL_MUTED)).toString()
                palette.darkmuted = data.getString(data.getColumnIndex(SetDB.tblPalette.COL_DARKMUTED)).toString()
                palette.img = data.getString(data.getColumnIndex(SetDB.tblPalette.COL_IMG)).toString()
                palette.darkvibrant = data.getString(data.getColumnIndex(SetDB.tblPalette.COL_DARKVIBRANT)).toString()
                palette.dominant = data.getString(data.getColumnIndex(SetDB.tblPalette.COL_DOMINANT)).toString()
                palette.userId = data.getString(data.getColumnIndex(SetDB.tblPalette.COL_USERID)).toInt()

                List.add(palette)

                //SE MUEVE A LA SIGUIENTE POSICION, REGRESA FALSO SI SE PASO DE LA CANTIDAD DE DATOS
            } while (data.moveToNext())

        }
        return List
    }


}