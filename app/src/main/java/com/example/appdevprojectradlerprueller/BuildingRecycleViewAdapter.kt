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

class BuildingRecycleViewAdapter (var context: Context, var buildings: ArrayList<Building>, buyBtnListener:BuildingFragment.BuyBtnListener):
    RecyclerView.Adapter<BuildingRecycleViewAdapter.BuildingViewHolder>() {
    private var myBuyBtnListener: BuildingFragment.BuyBtnListener
    init {
        var context = context
        var buildings = buildings
        myBuyBtnListener = buyBtnListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): BuildingViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.fragment_building, parent, false)
        return BuildingViewHolder(view, myBuyBtnListener)
    }

    override fun getItemCount(): Int {
        return buildings.size
    }

    override fun onBindViewHolder(holder: BuildingViewHolder, position: Int) {
        holder.image.setImageResource(buildings[position].image)
        holder.name.text = buildings[position].name
        holder.desc.text = buildings[position].desc
        holder.cost.text = getPrefixCost(buildings[position].cost)
        holder.cps.text = buildings[position].cps.toString()
        holder.amount.text = buildings[position].amount.toString()
        holder.building = buildings[position]
        holder.frame.drawable.isFilterBitmap = false
        holder.image.drawable.isFilterBitmap = false
    }

    class BuildingViewHolder(view: View, buyBtnListener:BuildingFragment.BuyBtnListener): RecyclerView.ViewHolder(view) {
        var frame: ImageView
        var image: ImageView
        var name: TextView
        var desc: TextView
        var cost: AppCompatButton
        var cps: TextView
        var amount: TextView
        var imageButton: AppCompatButton

        //correct Building will be set in onBindViewHolder()
        lateinit var building: Building

        init {
            frame = view.findViewById(R.id.buildingFrame)
            image = view.findViewById(R.id.buildingIcon)
            name = view.findViewById(R.id.buildingName)
            desc = view.findViewById(R.id.buildingDesc)
            cost = view.findViewById(R.id.imageButton)
            cps = view.findViewById(R.id.buildingCps)
            amount = view.findViewById(R.id.buildingAmount)
            imageButton = view.findViewById(R.id.imageButton)
            imageButton.setOnClickListener {
                Log.e("buyButton", "logged inside of fragment, $building")
                Log.e("WTF", clips.toString())
                if (buildings[position].cost.toBigInteger() > clips)
                {
                    Log.e("Error", "Not enough Clips")
                }
                else
                {
                    building.amount++
                    buildingCps = buildingCps.add(building.cps.toBigInteger())
                    clips = clips.subtract(building.cost.toBigInteger())
                }
                runBlocking { buyBtnListener.buyBtnPressed(building) }
            }
        }
    }

    fun getPrefixCost(cost: String): String
    {
        //TODO fix commas
        var _cost = cost.toBigInteger()
        when(_cost)
        {
            in 1.toBigInteger()..1000.toBigInteger() -> return cost
            in 1001.toBigInteger()..999999.toBigInteger() -> return "${(_cost.divide(1000.toBigInteger()))}K"
            in 1000000.toBigInteger()..999999999.toBigInteger() -> return "${(_cost/1000000.toBigInteger())}M"
            in 1000000000.toBigInteger()..999999999999.toBigInteger() -> return "${(_cost/1000000000.toBigInteger())}B"
        }
        return ""
    }
}