package com.ej.snackapp

class UserSnackInfo {

    var id : Int

    var name : String

    var food : String
    var foodOption : String

    var drink : String
    var drinkOption : String

    constructor(
        id: Int,
        name: String,
        food: String,
        foodOption: String,
        drink: String,
        drinkOption: String
    ) {
        this.id = id
        this.name = name
        this.food = food
        this.foodOption = foodOption
        this.drink = drink
        this.drinkOption = drinkOption
    }
}