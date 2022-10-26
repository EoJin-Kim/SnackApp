package com.ej.snackapp.dto.response

import com.ej.snackapp.enums.ResponseStatus
import com.squareup.moshi.Json

data class ApiResponse<T>(
    @Json(name = "status")
    var status: ResponseStatus,
    @Json(name = "data")
    var data:T
)