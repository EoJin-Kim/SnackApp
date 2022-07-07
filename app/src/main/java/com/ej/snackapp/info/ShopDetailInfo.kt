package com.ej.snackapp.info

class ShopDetailInfo {
    var shopName:String
    var snackType : String
    var menuURI:String
    var snackList : ArrayList<String>

    constructor(
        shopName: String,
        snackType: String,
        menuURI: String,
        snackList: ArrayList<String>
    ) {
        this.shopName = shopName
        this.snackType = snackType
        this.menuURI = menuURI
        this.snackList = snackList
    }
}