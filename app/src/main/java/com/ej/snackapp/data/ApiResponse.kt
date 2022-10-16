package com.ej.snackapp.data

import com.squareup.moshi.Json

data class ApiResponse<T>(
    @Json(name = "status")
    var status: ResponseStatus,
    @Json(name = "data")
    var data:T
) {

}