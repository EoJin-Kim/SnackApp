package com.ej.snackapp.api

import com.ej.snackapp.dto.request.MemberPickDto
import com.ej.snackapp.dto.request.SnackShopDto
import com.ej.snackapp.dto.response.*
import com.ej.snackapp.enums.SnackType
import retrofit2.http.*

interface SnackApi {
    companion object{
        const val firstUrl ="/api"
    }

    @GET("${firstUrl}/snack")
    suspend fun getShopId() : ApiResponse<ShopIdDto>

    @GET("${firstUrl}/snack/pick")
    suspend fun getUserPickInfo() : ApiResponse<MutableList<MemberSnackInfoDto>>

    @GET("${firstUrl}/shop/{snackType}")
    suspend fun getShopInfo(@Path("snackType") snackType: SnackType): ApiResponse<MutableList<ShopInfoDto>>

    @GET("${firstUrl}/shop/DRINK")
    suspend fun getDrinkShopInfo(): ApiResponse<MutableList<ShopInfoDto>>

    @PUT("${firstUrl}/snack/snackshop")
    suspend fun updateSnackShop(@Body snackShopDto: SnackShopDto): ApiResponse<String>

    @GET("${firstUrl}/shop/{snackType}/{shopId}")
    suspend fun getShopDetailInfo(@Path("snackType") snackType: SnackType, @Path("shopId") shopId: Long):ApiResponse<ShopDetailInfo>

    @POST("${firstUrl}/snack/pick")
    suspend fun setMemberSnack(@Body memberPickDto : MemberPickDto):ApiResponse<MutableList<MemberSnackInfoDto>>

}