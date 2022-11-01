package com.ej.snackapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ej.snackapp.api.SnackApi
import com.ej.snackapp.dto.response.ShopDetailInfo
import com.ej.snackapp.dto.request.MemberPickDto
import com.ej.snackapp.dto.request.SnackShopDto
import com.ej.snackapp.dto.response.MemberSnackInfoDto
import com.ej.snackapp.dto.response.ShopInfoDto
import com.ej.snackapp.enums.SnackType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val snackApi: SnackApi
) : ViewModel() {

    val userPickInfo = MutableLiveData<MutableList<MemberSnackInfoDto>>()
    val foodShopInfo = MutableLiveData<MutableList<ShopInfoDto>>()
    val drinkShopInfo = MutableLiveData<MutableList<ShopInfoDto>>()
    val shopDetailInfo = MutableLiveData<ShopDetailInfo>()
    val shopUpdateCheck = MutableLiveData<String>()
    var foodShopId = 0L
    var foodShopName:String? = ""
    var drinkShopId = 0L
    var drinkShopName:String? = ""

    init {
        viewModelScope.launch {
            val shopInfo = snackApi.getShopId().data!!
            foodShopId = shopInfo.foodId
            foodShopName = shopInfo.foodShopName
            drinkShopId = shopInfo.drinkId
            drinkShopName = shopInfo.drinkShopName
        }
    }

    fun fetchUserPickInfo() {
        viewModelScope.launch {
            val userSnackInfoResult = snackApi.getUserPickInfo()
            userPickInfo.value = userSnackInfoResult.data!!
        }
    }

    fun fetchShopInfo(snackType: SnackType) {
        viewModelScope.launch {
            if (snackType.equals(SnackType.FOOD)) {
                foodShopInfo.value = snackApi.getShopInfo(snackType).data!!
            } else if (snackType.equals(SnackType.DRINK)) {
                drinkShopInfo.value = snackApi.getShopInfo(snackType).data!!
            }
        }
    }

    fun selectSnackShop(foodShop: ShopInfoDto, drinkShop: ShopInfoDto) {
        viewModelScope.launch {
            shopUpdateCheck.value =
                snackApi.updateSnackShop(SnackShopDto(foodShop.id, drinkShop.id)).data!!
            this@MainViewModel.foodShopId = foodShop.id
            this@MainViewModel.foodShopName = foodShop.shopName
            this@MainViewModel.drinkShopId = drinkShop.id
            this@MainViewModel.drinkShopName = drinkShop.shopName
            fetchUserPickInfo()
        }
    }

    fun fetchShopMenuInfo(snackType: SnackType) {
        viewModelScope.launch {
            if (snackType == SnackType.FOOD) {
                shopDetailInfo.value = snackApi.getShopDetailInfo(snackType, foodShopId).data!!
            } else if (snackType == SnackType.DRINK) {
                shopDetailInfo.value = snackApi.getShopDetailInfo(snackType, drinkShopId).data!!
            }
        }
    }

    fun selectSnack(
        memberSnackInfoDto: MemberSnackInfoDto,
        snackType: SnackType,
        snackName: String
    ) {
        val memberPickDto = MemberPickDto(memberSnackInfoDto.id, snackType, snackName, "")
        viewModelScope.launch {
            userPickInfo.value = snackApi.setMemberSnack(memberPickDto).data!!
        }
    }
}