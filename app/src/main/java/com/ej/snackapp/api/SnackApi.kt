package com.ej.snackapp.api

import com.ej.snackapp.data.ApiResponse
import com.ej.snackapp.data.UserSnackInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface SnackApi {
    companion object{
        const val firstUrl ="/api/snack"
    }


    @GET("${firstUrl}/pick")
    suspend fun getUserPickInfo() : ApiResponse<ArrayList<UserSnackInfo>>
}