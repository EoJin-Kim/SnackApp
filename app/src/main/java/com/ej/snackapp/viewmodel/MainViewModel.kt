package com.ej.snackapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ej.snackapp.api.SnackApi
import com.ej.snackapp.dto.ApiResponse
import com.ej.snackapp.dto.ShopInfoDto
import com.ej.snackapp.dto.SnackType
import com.ej.snackapp.dto.UserSnackInfoDto
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
}