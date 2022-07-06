package com.ej.snackapp

class ShopInfo {
    val id : Int
    val shopName : String
    val menuURI : String

    constructor(id: Int, shopName: String, menuURI: String) {
        this.id = id
        this.shopName = shopName
        this.menuURI = menuURI
    }
}