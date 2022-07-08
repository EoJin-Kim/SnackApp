package com.ej.snackapp.info

import androidx.lifecycle.LiveData

class ShopDetailInfo {
    var shopName:String
    var snackType : String
    var menuURI:String
    var snackList : LiveData<ArrayList<String>>

    constructor(
        shopName: String,
        snackType: String,
        menuURI: String,
        snackList: LiveData<ArrayList<String>>
    ) {
        this.shopName = shopName
        this.snackType = snackType
        this.menuURI = menuURI
        this.snackList = snackList
    }
}