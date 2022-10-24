package com.ej.snackapp.dto.response

data class MemberPickResultDto(
    var id : Long,
    var name : String,
    var food : String,
    var drink:String,
    var foodOption : String,
    var drinkOption :String,
)
