package com.ej.snackapp.info

class ShopInfo {
    val id : Int
    val shopName : String
    val menuURI : String
    val shopType : String

    constructor(id: Int, shopName: String, menuURI: String, shopType: String) {
        this.id = id
        this.shopName = shopName
        this.menuURI = menuURI
        this.shopType = shopType
    }
}