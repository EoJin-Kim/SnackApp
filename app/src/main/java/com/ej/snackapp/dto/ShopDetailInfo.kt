package com.ej.snackapp.dto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

data class ShopDetailInfo(
    var shopName:String,
    var snackType : String,
    var menuURI:String,
    var snackList : MutableList<String>
) {
}