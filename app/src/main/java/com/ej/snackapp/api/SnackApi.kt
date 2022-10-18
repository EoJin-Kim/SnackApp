package com.ej.snackapp.api

import com.ej.snackapp.dto.ApiResponse
import com.ej.snackapp.dto.ShopInfoDto
import com.ej.snackapp.dto.SnackType
import com.ej.snackapp.dto.UserSnackInfoDto
import retrofit2.http.GET

interface SnackApi {
    companion object{
        const val firstUrl ="/api"
    }


    @GET("${firstUrl}/snack/pick")
    suspend fun getUserPickInfo() : ApiResponse<MutableList<UserSnackInfoDto>>

    @GET("${firstUrl}/shop/FOOD")
    suspend fun getFoodShopInfo(): ApiResponse<MutableList<ShopInfoDto>>

    @GET("${firstUrl}/shop/DRINK")
    suspend fun getDrinkShopInfo(): ApiResponse<MutableList<ShopInfoDto>>

}