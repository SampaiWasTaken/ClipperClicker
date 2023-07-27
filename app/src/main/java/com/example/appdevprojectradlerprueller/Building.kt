package com.example.appdevprojectradlerprueller

import java.math.BigInteger

/**
 * Building is a simple data class representing a building in the app.
 *
 * @param buildingID The unique identifier for the building.
 * @param image The resource ID of the building's image.
 * @param name The name of the building.
 * @param desc The description or details of the building.
 * @param cost The cost of the building as a String (formatted for display).
 * @param cps The amount of clips per second (cps) produced by the building.
 * @param amount The current number of buildings owned for this type.
 */
class Building constructor(buildingID: Int, image: Int, name: String, desc: String, cost: String, cps: Int, amount: Int) {
    var buildingID = buildingID
    var image = image
    var name = name
    var desc = desc
    var cost = cost
    var cps = cps
    var amount = amount

    /**
     * Returns a String representation of the Building object.
     *
     * @return A String containing all the properties of the Building object.
     */
    override fun toString(): String {
        return "Building(buildingID=$buildingID, image=$image, name='$name', desc='$desc', cost=$cost, cps=$cps, amount=$amount)"
    }


}
