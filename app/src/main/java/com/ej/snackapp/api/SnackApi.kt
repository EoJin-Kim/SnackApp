package com.ej.snackapp.api

import com.ej.snackapp.dto.*
import com.ej.snackapp.dto.response.ApiResponse
import com.ej.snackapp.dto.response.ShopIdDto
import com.ej.snackapp.dto.ShopDetailInfo
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface SnackApi {
    companion object{
        const val firstUrl ="/api"
    }

    @GET("${firstUrl}/snack")
    suspend fun getShopId() : ApiResponse<ShopIdDto>

    @GET("${firstUrl}/snack/pick")
    suspend fun getUserPickInfo() : ApiResponse<MutableList<UserSnackInfoDto>>

    @GET("${firstUrl}/shop/{snackType}")
    suspend fun getShopInfo(@Path("snackType") snackType: SnackType): ApiResponse<MutableList<ShopInfoDto>>

    @GET("${firstUrl}/shop/DRINK")
    suspend fun getDrinkShopInfo(): ApiResponse<MutableList<ShopInfoDto>>

    @PUT("${firstUrl}/snack/snackshop")
    suspend fun updateSnackShop(@Body snackShopDto: SnackShopDto): ApiResponse<String>

    @PUT("${firstUrl}/shop/FOOD/{shopId}")
    suspend fun getFoodShopDetailInfo(@Path("shopId") shopId: Long):ApiResponse<ShopDetailInfo>

    @PUT("${firstUrl}/shop/DRINK/{shopId}")
    suspend fun getDrinkShopDetailInfo(@Path("shopId") shopId: Long):ApiResponse<ShopDetailInfo>

}