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

    val foodShopDetailInfo = MutableLiveData<ShopDetailInfo>()
    val drinkShopDetailInfo = MutableLiveData<ShopDetailInfo>()

    val updateCheck = MutableLiveData<String>()

    var foodShopId = 0L
    var drinkShopId = 0L

    init {
        viewModelScope.launch {
            val shopId = snackApi.getShopId().data!!
            foodShopId = shopId.foodId
            drinkShopId = shopId.drinkId
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
                foodShopInfo.value = snackApi.getFoodShopInfo().data!!
            }
            else if(snackType.equals(SnackType.DRINK)){
                drinkShopInfo.value = snackApi.getDrinkShopInfo().data!!
            }

        }
    }

    fun selectSnackShop(foodShopId: Long, drinkShopId: Long) {
        viewModelScope.launch {
            updateCheck.value = snackApi.updateSnackShop(SnackShopDto(foodShopId,drinkShopId)).data!!
            this@MainViewModel.foodShopId = foodShopId
            this@MainViewModel.drinkShopId = drinkShopId
        }
    }
    fun fetchFoodShopMenuInfo(){
        viewModelScope.launch {
            foodShopDetailInfo.value = snackApi.getFoodShopDetailInfo(foodShopId).data!!
        }
    }

    fun fetchDrinkShopMenuInfo(){
        viewModelScope.launch {
            drinkShopDetailInfo.value = snackApi.getDrinkShopDetailInfo(foodShopId).data!!
        }
    }
}