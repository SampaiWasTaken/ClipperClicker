package com.example.appdevprojectradlerprueller

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.appdevprojectradlerprueller.databinding.ActivityMainBinding
import kotlinx.coroutines.runBlocking
import java.math.BigInteger

var clips: BigInteger = BigInteger.ZERO
var buildingCps: BigInteger = BigInteger.ONE
var clickValue: BigInteger = BigInteger.ONE
private var firstFrag = BuildingFragment()
lateinit var handler: Handler
lateinit var buildingRecyclerAdapter: BuildingRecycleViewAdapter
lateinit var clipsTxt: TextView
lateinit var clipImg: ImageView
lateinit var cpsTxt: TextView
lateinit var db: AppDatabase


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
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "buildings")
                .fallbackToDestructiveMigration()
                .build()
        runBlocking { dbAccess() }
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clipsTxt = binding.amountClips
        clipImg = binding.clipImg
        cpsTxt = binding.cpsAmount
        val recyler = binding.recycleView
        buildingRecyclerAdapter = BuildingRecycleViewAdapter(this, buildings, this)
        recyler.adapter = buildingRecyclerAdapter
        recyler.layoutManager = LinearLayoutManager(this)
        handler = Handler(Looper.getMainLooper())
        runBlocking { updateFrags() }
        cpsTxt.text = "$buildingCps"
        clips.inc()


        // idle animation of big paperclip
        val idleScreenShakeAnimator = IdleScreenShakeAnimator()
        val idleAnimation = idleScreenShakeAnimator.getScreenShakeAnimatorSet(20f,binding.clipImg)
        idleAnimation.start()

        //set ontouch listener on clipper picture for later use
        binding.clipImg.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val x = event.x.toInt()  // get x-Coordinate
                val y = event.y.toInt()  // get y-Coordinate
                Log.e("WTF", "testing $x , $y")
            }
            false
        }

        }

    suspend fun dbAccess() {

        val dao = db.buildingDao()
        //dao.nukeTable()
        if (dao.getAll().isEmpty()) {
            dao.insertAll(
                buildingEntity(1, R.drawable.clip_factory_worker, "Factory Worker", "A minimum wage worker to produce paperclips for you", "150", 1, 0),
                buildingEntity(2, R.drawable.clip_factory, "Paperclip Factory", "A factory that produces paperclips", "1000", 10, 0),
                buildingEntity(3, R.drawable.clip_tree, "Paperclip Plantation", "Grow more paperclips from planting some, it's renewable!", "11000", 80, 0),
                buildingEntity(4, R.drawable.clip_clippy, "Clippy", "This is not a copyright infringement", "120000", 470, 0),
                buildingEntity(5, R.drawable.clip_blood, "Blood Altar", "Produce paperclips with dark magic", "1300000", 2600, 0),
                buildingEntity(6, R.drawable.clip_alien, "Steve","His name is Steve, he sells paperclips from another world", "14000000", 14000, 0),
                buildingEntity(7, R.drawable.clip_god, "God", "Pray for paperclips", "200000000", 78000, 0)
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
        val cpsString = sharedPref.getString(getString(R.string.cps), "1")
        if (cpsString != null) {
            buildingCps = cpsString.toBigInteger()
        }
        if (clipString != null) {
            clips = clipString.toBigInteger()
        }
        handler.post(incBuildingCps)
        clipsTxt.text = clips.toString()
        cpsTxt.text = buildingCps.toString()
        runBlocking { updateFrags() }
    }

    override fun onPause() {
        super.onPause()

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(getString(R.string.clips), clips.toString())
            putString(getString(R.string.cps), buildingCps.toString())
            apply()
        }
        handler.removeCallbacks(incBuildingCps)
    }

    override fun onDestroy() {
        super.onDestroy()

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(getString(R.string.clips), clips.toString())
            putString(getString(R.string.cps), buildingCps.toString())
            apply()
        }
        handler.removeCallbacks(incBuildingCps)
    }

    override suspend fun buyBtnPressed(building: Building) {
        Log.d("buyButton", "printed pressed Upgrade Building in MAIN: $building")
        val newCost: Int = (building.cost.toDouble()*(Math.pow(1.15, building.amount.toDouble()))).toInt()
        db.buildingDao().updateById(building.amount, newCost, building.buildingID)
        cpsTxt.text = buildingCps.toString()
        clipsTxt.text = clips.toString()
        updateFrags()

    }

    suspend fun updateFrags()
    {

        val bents = db.buildingDao().getAll()
        buildings.clear()
        for (b in bents) {
            Log.e("building", "$b")
            buildings.add(Building(b.buildingID, b.img, b.name, b.desc, b.cost, b.cps, b.amount))
        }
        val recyler = binding.recycleView
        buildingRecyclerAdapter = BuildingRecycleViewAdapter(this, buildings, this)
        recyler.adapter = buildingRecyclerAdapter
        buildingRecyclerAdapter.notifyDataSetChanged()
    }
}