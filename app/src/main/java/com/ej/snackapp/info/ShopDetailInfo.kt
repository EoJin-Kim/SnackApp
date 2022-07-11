package com.ej.snackapp.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ShopDetailInfo {
    var shopName:String
    var snackType : String
    var menuURI:String
    var snackList : MutableLiveData<ArrayList<String>>

    constructor(
        shopName: String,
        snackType: String,
        menuURI: String,
        snackList: MutableLiveData<ArrayList<String>>
    ) {
        this.shopName = shopName
        this.snackType = snackType
        this.menuURI = menuURI
        this.snackList = snackList
    }
}