package com.ej.snackapp.dto.request

import com.ej.snackapp.enums.SnackType

data class MemberPickDto(
    val memberId:Long,
    val snackType: SnackType,
    val snack:String,
    val option:String
)
