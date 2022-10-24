package com.ej.snackapp.api

import com.ej.snackapp.dto.*
import com.ej.snackapp.dto.response.ApiResponse
import com.ej.snackapp.dto.response.ShopIdDto
import com.ej.snackapp.dto.ShopDetailInfo
import com.ej.snackapp.dto.request.MemberPickDto
import retrofit2.http.*

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

    @GET("${firstUrl}/shop/{snackType}/{shopId}")
    suspend fun getShopDetailInfo(@Path("snackType") snackType: SnackType,@Path("shopId") shopId: Long):ApiResponse<ShopDetailInfo>

    @POST("${firstUrl}/shop/{snackType}/{shopId}")
    suspend fun setMemberSnack(@Body memberPickDto : MemberPickDto):ApiResponse<MemberPickDto>

}