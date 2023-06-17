package com.example.appdevprojectradlerprueller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BuildingRecycleViewAdapter (var context: Context, var buildings: ArrayList<Building>):
    RecyclerView.Adapter<BuildingRecycleViewAdapter.BuildingViewHolder>() {
    init {
        var context = context
        var buildings = buildings
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildingViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.fragment_building, parent, false)
        return BuildingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return buildings.size
    }

    override fun onBindViewHolder(holder: BuildingViewHolder, position: Int) {
        holder.image.setImageResource(buildings[position].image)
        holder.name.text = buildings[position].name
        holder.desc.text = buildings[position].desc
        holder.cost.text = buildings[position].cost.toString()
        holder.cps.text = buildings[position].cps.toString()
        holder.amount.text = buildings[position].amount.toString()

    }

    class BuildingViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var image: ImageView
        var name: TextView
        var desc: TextView
        var cost: TextView
        var cps: TextView
        var amount: TextView

        init {
            image = view.findViewById(R.id.buildingIcon)
            name = view.findViewById(R.id.buildingName)
            desc = view.findViewById(R.id.buildingDesc)
            cost = view.findViewById(R.id.buildingCost)
            cps = view.findViewById(R.id.buildingCps)
            amount = view.findViewById(R.id.buildingAmount)
        }
    }
}