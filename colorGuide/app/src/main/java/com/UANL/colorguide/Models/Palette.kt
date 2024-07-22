package com.UANL.colorguide.Models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Palette(var idPalette:Int? =  null,
                   var nameP:String? = null,
                   var img:String? =  null,
                   var vibrant:String? =  null,
                   var muted:String? =  null,
                   var darkmuted:String? =  null,
                   var darkvibrant:String? =  null,
                   var dominant:String? =  null,
                   var userId:Int? =  null){}

