package com.example.appdevprojectradlerprueller

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.appdevprojectradlerprueller.databinding.ActivityMainBinding
import kotlinx.coroutines.runBlocking
import java.math.BigInteger

var clips: BigInteger = BigInteger.ZERO
var buildingCps: BigInteger = BigInteger.ONE
var clickValue: BigInteger = BigInteger.ONE
private var firstFrag = BuildingFragment()
lateinit var handler: Handler
lateinit var clipsTxt: TextView
lateinit var clipImg: ImageView
lateinit var cpsTxt: TextView


var buildings = ArrayList<Building>()


private val incBuildingCps = object : Runnable {
    override fun run() {
        clips = clips.add(buildingCps)
        handler.postDelayed(this, 1000)
        clipsTxt.text = "$clips"
        //Log.d("clips", clips.toString())
    }
}

class MainActivity : AppCompatActivity(), BuildingFragment.BuyBtnListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        runBlocking { dbAccess() }
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clipsTxt = binding.amountClips
        clipImg = binding.clipImg
        cpsTxt = binding.cpsAmount
        val recyler = binding.recycleView
        val buildingRecyclerAdapter = BuildingRecycleViewAdapter(this, buildings, this)
        recyler.adapter = buildingRecyclerAdapter
        recyler.layoutManager = LinearLayoutManager(this)
        handler = Handler(Looper.getMainLooper())

        cpsTxt.text = "$buildingCps"
        clips.inc()
    }

    suspend fun dbAccess() {
        val db =
            Room.databaseBuilder(applicationContext, AppDatabase::class.java, "buildings").build()
        val dao = db.buildingDao()

        if (dao.getAll().isEmpty()) {
            dao.insertAll(
                buildingEntity(1, R.drawable.frame, "Leon", "Mega Nice", 1, 1, 0),
                buildingEntity(2, R.drawable.frame, "Sam", "Mega Sick", 2, 2, 0),
                buildingEntity(3, R.drawable.frame, "asdasdasd", "Mega Sick", 2, 2, 0),
                buildingEntity(4, R.drawable.frame, "asdasdasd", "Mega Sick", 2, 2, 0),
                buildingEntity(5, R.drawable.frame, "Tom Turbo", "Mega Sick", 2, 2, 0)
            )
        }
//        dao.updateById(9, 1234, 5)
        val bents = dao.getAll()
        for (b in bents) {
            buildings.add(Building(b.buildingID, b.img, b.name, b.desc, b.cost, b.cps, b.amount))
        }
    }

    fun clipperClicked(view: View) {
        clips = clips.add(clickValue)
        clipsTxt.text = "$clips"
        //Log.d("clips", clips.toString())
    }

    override fun onResume() {
        super.onResume()

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        val clipString = sharedPref.getString(getString(R.string.clips), "0")
        if (clipString != null) {
            clips = clipString.toBigInteger()
        }
        handler.post(incBuildingCps)
    }

    override fun onPause() {
        super.onPause()

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(getString(R.string.clips), clips.toString())
            apply()
        }
        handler.removeCallbacks(incBuildingCps)
    }

    override fun onDestroy() {
        super.onDestroy()

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(getString(R.string.clips), clips.toString())
            apply()
        }
        handler.removeCallbacks(incBuildingCps)
    }

    override fun buyBtnPressed(building: Building) {
        Log.d("buyButton", "printed pressed Upgrade Building in MAIN: $building")
    }
}