package com.ej.snackapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ej.snackapp.api.SnackApi
import com.ej.snackapp.dto.*
import com.ej.snackapp.dto.ShopDetailInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val snackApi: SnackApi
) : ViewModel(){

    val userPickInfo = MutableLiveData<MutableList<UserSnackInfoDto>>()
    val foodShopInfo = MutableLiveData<MutableList<ShopInfoDto>>()
    val drinkShopInfo = MutableLiveData<MutableList<ShopInfoDto>>()

    val shopDetailInfo = MutableLiveData<ShopDetailInfo>()

    val shopUpdateCheck = MutableLiveData<String>()

    var foodShopId = 0L
    var foodShopName = ""
    var drinkShopId = 0L
    var drinkShopName = ""

    init {
        viewModelScope.launch {
            val shopInfo = snackApi.getShopId().data!!
            foodShopId = shopInfo.foodId
            foodShopName = shopInfo.foodShopName
            drinkShopId = shopInfo.drinkId
            drinkShopName = shopInfo.drinkShopName
        }
    }

    fun fetchUserPickInfo(){
        viewModelScope.launch{
            val userSnackInfoResult = snackApi.getUserPickInfo()
            userPickInfo.value = userSnackInfoResult.data!!
        }
    }

    fun fetchShopInfo(snackType: SnackType){
        viewModelScope.launch {
            if(snackType.equals(SnackType.FOOD)){
                foodShopInfo.value = snackApi.getShopInfo(snackType).data!!
            }
            else if(snackType.equals(SnackType.DRINK)){
                drinkShopInfo.value = snackApi.getShopInfo(snackType).data!!
            }
        }
    }

    fun selectSnackShop(foodShopId: Long, drinkShopId: Long) {
        viewModelScope.launch {
            shopUpdateCheck.value = snackApi.updateSnackShop(SnackShopDto(foodShopId,drinkShopId)).data!!
            this@MainViewModel.foodShopId = foodShopId
            this@MainViewModel.drinkShopId = drinkShopId
        }
    }

    fun fetchShopMenuInfo(snackType: SnackType){
        viewModelScope.launch {
            shopDetailInfo.value = snackApi.getShopDetailInfo(snackType,foodShopId).data!!
        }
    }

    fun selectSnack(){
        viewModelScope.launch {
//            snackApi.setMemberSnack().data!!
        }
    }
}