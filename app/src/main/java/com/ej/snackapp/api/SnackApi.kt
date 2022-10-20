package com.ej.snackapp.api

import com.ej.snackapp.dto.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

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

    @PUT("${firstUrl}/snack/snackshop")
    suspend fun updateSnackShop(@Body snackShopDto: SnackShopDto): ApiResponse<String>

}