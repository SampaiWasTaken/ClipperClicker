package com.example.appdevprojectradlerprueller

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.runBlocking
import java.math.BigInteger

/**
 * BuildingRecycleViewAdapter is a custom RecyclerView adapter used to display a list of buildings
 * in a RecyclerView. It handles binding data to individual views and handles user interaction
 * through the buy button.
 *
 * @param context The context of the calling activity or fragment.
 * @param buildings An ArrayList of Building objects representing the data to be displayed.
 * @param buyBtnListener An instance of BuildingFragment.BuyBtnListener to handle buy button clicks.
 */
class BuildingRecycleViewAdapter(
    var context: Context,
    var buildings: ArrayList<Building>,
    buyBtnListener: BuildingFragment.BuyBtnListener
) : RecyclerView.Adapter<BuildingRecycleViewAdapter.BuildingViewHolder>() {

    /**
     * Private member variable to store the BuyBtnListener instance.
     */
    private var myBuyBtnListener: BuildingFragment.BuyBtnListener

    /**
     * Initialization block to set up the adapter.
     */
    init {
        var context = context
        var buildings = buildings
        myBuyBtnListener = buyBtnListener
    }

    /**
     * Called when RecyclerView needs a new ViewHolder to represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new BuildingViewHolder that holds a View for the building item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildingViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.fragment_building, parent, false)
        return BuildingViewHolder(view, myBuyBtnListener)
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of buildings in the dataset.
     */
    override fun getItemCount(): Int {
        return buildings.size
    }

    /**
     * Called to bind the data from the specified position to the given ViewHolder.
     *
     * @param holder The ViewHolder to bind the data to.
     * @param position The position of the item in the data set.
     */
    override fun onBindViewHolder(holder: BuildingViewHolder, position: Int) {
        val building = buildings[position]

        holder.image.setImageResource(building.image)
        holder.image.drawable.isFilterBitmap = false
        holder.name.text = building.name
        holder.desc.text = building.desc
        holder.cost.text = getPrefixCost(building.cost)
        holder.cps.text = building.cps.toString()
        holder.amount.text = building.amount.toString()
        holder.building = building
        holder.frame.drawable.isFilterBitmap = false
        holder.image.drawable.isFilterBitmap = false
    }

    /**
     * ViewHolder class for holding the views of each building item in the RecyclerView.
     *
     * @param view The View for the building item.
     * @param buyBtnListener An instance of BuildingFragment.BuyBtnListener to handle buy button clicks.
     */
    class BuildingViewHolder(view: View, buyBtnListener: BuildingFragment.BuyBtnListener) :
        RecyclerView.ViewHolder(view) {

        // Views within the item layout
        var frame: ImageView
        var image: ImageView
        var name: TextView
        var desc: TextView
        var cost: AppCompatButton
        var cps: TextView
        var amount: TextView
        var imageButton: AppCompatButton

        // Correct Building will be set in onBindViewHolder()
        lateinit var building: Building

        /**
         * Initialization block to set up the views and click listener for the buy button.
         */
        init {
            frame = view.findViewById(R.id.buildingFrame)
            image = view.findViewById(R.id.buildingIcon)
            name = view.findViewById(R.id.buildingName)
            desc = view.findViewById(R.id.buildingDesc)
            cost = view.findViewById(R.id.imageButton)
            cps = view.findViewById(R.id.buildingCps)
            amount = view.findViewById(R.id.buildingAmount)
            imageButton = view.findViewById(R.id.imageButton)

            // Click listener for the buy button
            imageButton.setOnClickListener {
                // Check if there are enough "clips" to buy the building
                if (buildings[position].cost.toBigInteger() > clips) {
                    Log.e("Error", "Not enough Clips")
                } else {
                    building.amount++
                    buildingCps = buildingCps.add(building.cps.toBigInteger())
                    clips = clips.subtract(building.cost.toBigInteger())

                    // Notify the listener that the buy button is pressed with the selected building
                    runBlocking { buyBtnListener.buyBtnPressed(building) }
                }
            }
        }
    }

    /**
     * Helper function to format the cost of a building based on its value.
     *
     * @param cost The cost of the building as a String.
     * @return The formatted cost string with appropriate prefixes (K, M, B) based on the value.
     */
    fun getPrefixCost(cost: String): String {
        var _cost = cost.toBigDecimal()
        when (_cost) {
            in 1.toBigDecimal()..1000.toBigDecimal() -> return cost
            in 1001.toBigDecimal()..999999.toBigDecimal() -> return String.format("%.1f", _cost.divide(1000.toBigDecimal())) + "K"
            in 1000000.toBigDecimal()..999999999.toBigDecimal() -> return String.format("%.1f", _cost.divide(1000000.toBigDecimal())) + "M"
            in 1000000000.toBigDecimal()..999999999999.toBigDecimal() -> return String.format("%.1f", _cost.divide(1000000000.toBigDecimal())) + "B"
        }
        return ""
    }
}