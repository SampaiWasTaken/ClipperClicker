package com.example.appdevprojectradlerprueller


class Building constructor(buildingID: Int, image: Int, name: String, desc: String, cost: Int, cps: Int, amount: Int) {
    var buildingID = buildingID
    var image = image
    var name = name
    var desc = desc
    var cost = cost
    var cps = cps
    var amount = amount
    override fun toString(): String {
        return "Building(buildingID=$buildingID, image=$image, name='$name', desc='$desc', cost=$cost, cps=$cps, amount=$amount)"
    }


}
