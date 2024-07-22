package com.UANL.colorguide.Models


//Las variables se declaran como opcionales.
//Esto permite crear un objecto de album por defecto vacio
//que sea usa cuando vamos agregar un nuevo album

data class Usuarios(var idUser:Int? =  null,
                    var name:String? = null,
                    var lastName:String? =  null,
                    var mail: String? = null,
                    var password: String? = null,
                    var avatar:String? =  null){}