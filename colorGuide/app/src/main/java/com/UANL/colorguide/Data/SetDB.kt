package com.UANL.colorguide.Data

class SetDB {

    //DECLARAMOS  EL NOMBRE Y VERSION DE TAL FOR QUE PUEDA SER VISIBLES PARA CUALQUIER CLASE
    companion object{
        val DB_NAME =  "palette.db"
        val DB_VERSION =  1
    }

    //VAMOS ES DEFINIR EL ESQUEMA DE UNA DE LAS TABLAS
    abstract class tblPalette{
        //DEFINIMOS LOS ATRIBUTOS DE LA CLASE USANDO CONTANTES
        companion object{
            val TABLE_NAME = "palette"
            val COL_IDPALETTE =  "idPalette"
            val COL_NAMEP =  "nameP"
            val COL_VIBRANT =  "vibrant"
            val COL_MUTED = "muted"
            val COL_DARKMUTED =  "darkmuted"
            val COL_IMG =  "img" // byte Array image
            val COL_DARKVIBRANT =  "darkvibrant"
            val COL_DOMINANT =  "dominant"
            val COL_USERID =  "userId"
        }
    }
}