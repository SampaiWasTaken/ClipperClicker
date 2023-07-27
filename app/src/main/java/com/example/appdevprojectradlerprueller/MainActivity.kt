package com.example.appdevprojectradlerprueller

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.appdevprojectradlerprueller.databinding.ActivityMainBinding
import kotlinx.coroutines.runBlocking
import java.math.BigInteger
import java.util.*
import kotlin.math.atan
import kotlin.math.log
import kotlin.math.max
import kotlin.math.sqrt


// Global variables
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
private var sensorManager: SensorManager? = null
private var totalSteps = 0f
private var currentSteps = 0f
private var previousTotalSteps = 0f

// List of buildings
var buildings = ArrayList<Building>()

// Runnable object to increment buildingCps and update clipsTxt
private val incBuildingCps = object : Runnable {
    override fun run() {
        clips = clips.add(buildingCps)
        handler.postDelayed(this, 1000)
        clipsTxt.text = "$clips"
    }
}

/**
 * MainActivity class: The main activity of the app that manages the game mechanics and UI interactions.
 */
class MainActivity : AppCompatActivity(), BuildingFragment.BuyBtnListener, SensorEventListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var gestureDetector: GestureDetector

    /**
     * onCreate function: Called when the activity is created. Initializes the UI and sets up the game.
     * @param savedInstanceState: The previously saved instance state, if available.
     */
    @SuppressLint("ClickableViewAccessibility", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up the Room database for buildings
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "buildings")
            .fallbackToDestructiveMigration()
            .build()

        // Load building data from the database
        runBlocking { dbAccess() }

        // Set the layout using ViewBinding
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize UI elements and building recycler view
        clipsTxt = binding.amountClips
        clipImg = binding.clipImg
        cpsTxt = binding.cpsAmount
        val recycler = binding.recycleView
        buildingRecyclerAdapter = BuildingRecycleViewAdapter(this, buildings, this)
        recycler.adapter = buildingRecyclerAdapter
        recycler.layoutManager = LinearLayoutManager(this)

        // Initialize handler and start incrementing buildingCps
        handler = Handler(Looper.getMainLooper())
        runBlocking { updateFrags() }
        cpsTxt.text = "$buildingCps"
        clips.inc()
        clipImg.drawable.isFilterBitmap = false

        // Set up the sensor manager to detect steps
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Create the idle animation for the big paperclip
        val idleScreenShakeAnimator = IdleScreenShakeAnimator()
        val idleAnimation = idleScreenShakeAnimator.getScreenShakeAnimatorSet(20f, binding.clipImg)
        idleAnimation.start()

        // Create GestureDetector to handle touch gestures on the paperclip
        val gestureDetector =
            GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDown(e: MotionEvent): Boolean {
                    // Return true to enable gestures to work properly
                    return true
                }

                override fun onSingleTapUp(event: MotionEvent): Boolean {
                    // Handle single tap on the paperclip
                    clipperClicked()
                    clipperClickedAnimation(event)
                    return true
                }

                override fun onFling(
                    e1: MotionEvent,
                    e2: MotionEvent,
                    velocityX: Float,
                    velocityY: Float
                ): Boolean {
                    // Handle fling gesture on the paperclip

                    // Increment clips based on clickValue
                    clips = clips.add(clickValue)
                    clipsTxt.text = "$clips"

                    // Calculate the vector of the fling gesture
                    val vx = e2.x - e1.x
                    val vy = e2.y - e1.y
                    val vectorAngleRadiant = atan(vy / vx)
                    var vectorAngleDegree = 0.0

                    // Calculate the angle in degrees based on the quadrant of the vector
                    if (vy <= 0) {
                        if (vx <= 0) {
                            // Third quadrant
                            vectorAngleDegree = vectorAngleRadiant / (Math.PI / 180)
                        } else {
                            // Fourth quadrant
                            vectorAngleDegree = vectorAngleRadiant / (Math.PI / 180) - 180
                        }
                    } else {
                        if (vx >= 0) {
                            // Second quadrant
                            vectorAngleDegree = vectorAngleRadiant / (Math.PI / 180) - 180
                        } else {
                            // First quadrant
                            vectorAngleDegree = vectorAngleRadiant / (Math.PI / 180)
                        }
                    }

                    // Calculate the midpoint of the fling
                    val x = (e1.x.toInt() + vx / 2).toInt()
                    val y = (e1.y.toInt() + vy / 2).toInt()

                    // Create and display the fling animation image view
                    val lp = ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    )
                    val iv = ImageView(applicationContext)
                    lp.setMargins(x, y, 0, 0)
                    iv.layoutParams = lp
                    iv.rotation = vectorAngleDegree.toFloat()
                    iv.scaleX = 3f
                    iv.scaleY = 3f
                    lateinit var clickAnimation: AnimationDrawable
                    iv.apply {
                        setBackgroundResource(R.drawable.animated_sling)
                        clickAnimation = background as AnimationDrawable
                    }
                    clickAnimation.start()
                    (binding.relativeLayout as ViewGroup).addView(iv)

                    // Remove the fling animation image view after a short delay
                    Handler().postDelayed({
                        binding.relativeLayout.post {
                            binding.relativeLayout.removeView(iv)
                        }
                    }, 250)

                    return super.onFling(e1, e2, velocityX, velocityY)
                }
            })
        gestureDetector.setOnDoubleTapListener(null)

        // Set the gesture detector on the main layout to capture touch events
        binding.relativeLayout.setOnTouchListener { view, event ->
            return@setOnTouchListener gestureDetector.onTouchEvent(event)
        }
    }

    /**
     * dbAccess function: A suspend function to access the Room database and load building data.
     * Uses coroutines to run the database access in a background thread.
     */
    suspend fun dbAccess() {
        val dao = db.buildingDao()

        // Insert default building data if the database is empty
        if (dao.getAll().isEmpty()) {
            dao.insertAll(
                buildingEntity(
                    1, R.drawable.clip_factory_worker, "Factory Worker",
                    "A minimum wage worker to produce paperclips for you", "150", 1, 0
                ),
                buildingEntity(
                    2, R.drawable.clip_factory, "Paperclip Factory",
                    "A factory that produces paperclips", "1000", 10, 0
                ),
                buildingEntity(
                    3, R.drawable.clip_tree, "Paperclip Plantation",
                    "Grow more paperclips from planting some, it's renewable!", "11000", 80, 0
                ),
                buildingEntity(
                    4, R.drawable.clip_clippy, "Clippy",
                    "This is not a copyright infringement", "120000", 470, 0
                ),
                buildingEntity(
                    5, R.drawable.clip_blood, "Blood Altar",
                    "Produce paperclips with dark magic", "1300000", 2600, 0
                ),
                buildingEntity(
                    6,
                    R.drawable.clip_alien,
                    "Steve",
                    "His name is Steve, he sells paperclips from another world",
                    "14000000",
                    14000,
                    0
                ),
                buildingEntity(
                    7, R.drawable.clip_god, "God", "Pray for paperclips",
                    "200000000", 78000, 0
                )
            )
        }

        // Load building data from the database into the buildings list
        val bents = dao.getAll()
        for (b in bents) {
            buildings.add(Building(b.buildingID, b.img, b.name, b.desc, b.cost, b.cps, b.amount))
        }
    }

    /**
     * clipperClicked function: Called when the paperclip image is clicked.
     * Increments the number of clips and updates the clipsTxt.
     */
    fun clipperClicked() {
        clips = clips.add(clickValue)
        clipsTxt.text = "$clips"
    }

    /**
     * clipperClickedAnimation function: Animates the click effect on the paperclip image.
     * @param event: The touch event containing the click coordinates.
     */
    fun clipperClickedAnimation(event: MotionEvent) {
        clips = clips.add(clickValue)
        clipsTxt.text = "$clips"

        // Get the click coordinates
        val x = event.x.toInt()
        val y = event.y.toInt()

        // Create and display the click animation image view
        val lp = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        val iv = ImageView(applicationContext)
        lp.setMargins(x, y, 0, 0)
        iv.layoutParams = lp
        iv.scaleX = 3f
        iv.scaleY = 3f
        lateinit var clickAnimation: AnimationDrawable
        iv.apply {
            setBackgroundResource(R.drawable.animated_click)
            clickAnimation = background as AnimationDrawable
        }
        clickAnimation.start()
        (binding.relativeLayout as ViewGroup).addView(iv)

        // Remove the click animation image view after a short delay
        Handler().postDelayed({
            binding.relativeLayout.post {
                binding.relativeLayout.removeView(iv)
            }
        }, 250)
    }

    /**
     * onResume function: Called when the activity is resumed.
     * Registers the step counter sensor and resumes the game mechanics.
     */
    override fun onResume() {
        super.onResume()

        // Register the step counter sensor
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null) {
            Toast.makeText(this, "No step sensor detected on this device", Toast.LENGTH_SHORT)
                .show()
        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }

        // Retrieve and update saved game data
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        val clipString = sharedPref.getString(getString(R.string.clips), "0")
        val cpsString = sharedPref.getString(getString(R.string.cps), "1")
        previousTotalSteps = sharedPref.getFloat("2", 0f)
        totalSteps = sharedPref.getFloat("3", 0f)


        // Show a toast message with the number of steps taken and clips earned
        currentSteps = max(0f, totalSteps - previousTotalSteps)
        Toast.makeText(
            this,
            "You have taken ${currentSteps} steps since your last visit, gaining you ${currentSteps} clips!",
            Toast.LENGTH_LONG
        ).show()
        previousTotalSteps = totalSteps
        // Update global variables with saved data
        if (cpsString != null) {
            buildingCps = cpsString.toBigInteger()
        }
        if (clipString != null) {
            clips = clipString.toBigInteger()
        }
        clips.add(currentSteps.toInt().toBigInteger())
        handler.post(incBuildingCps)
        clipsTxt.text = clips.toString()
        cpsTxt.text = buildingCps.toString()
        runBlocking { updateFrags() }
    }

    /**
     * onSensorChanged function: Called when the sensor data changes (step counter sensor).
     * Updates the totalSteps and currentSteps variables based on the sensor data.
     * @param p0: The SensorEvent containing the sensor data.
     */
    override fun onSensorChanged(p0: SensorEvent?) {
        totalSteps = p0!!.values[0]
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putFloat("3", totalSteps)
            apply()
        }
    }

    /**
     * onAccuracyChanged function: Called when the accuracy of the sensor changes.
     * @param p0: The Sensor reporting the accuracy change.
     * @param p1: The new accuracy value.
     */
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        return
    }

    /**
     * onPause function: Called when the activity is paused.
     * Saves the game data (clips, buildingCps, and previousTotalSteps) to SharedPreferences.
     */
    override fun onPause() {
        super.onPause()

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(getString(R.string.clips), clips.toString())
            putString(getString(R.string.cps), buildingCps.toString())
            putFloat("2", previousTotalSteps)
            apply()
        }
        handler.removeCallbacks(incBuildingCps)
    }

    /**
     * onDestroy function: Called when the activity is destroyed.
     * Saves the game data (clips, buildingCps, and previousTotalSteps) to SharedPreferences.
     */
    override fun onDestroy() {
        super.onDestroy()

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(getString(R.string.clips), clips.toString())
            putString(getString(R.string.cps), buildingCps.toString())
            Log.e("steps", "prev: ${previousTotalSteps}, total: ${totalSteps}")
            previousTotalSteps = totalSteps
            Log.e("steps", "${previousTotalSteps}")

            putFloat("2", previousTotalSteps)
            apply()
        }
        handler.removeCallbacks(incBuildingCps)
    }

    /**
     * buyBtnPressed function: Called when the "Buy" button in the building fragment is pressed.
     * Updates the building data in the database and UI after purchasing a building.
     * @param building: The Building object representing the building to be purchased.
     */
    override suspend fun buyBtnPressed(building: Building) {
        // Update building data in the database
        val newCost: Int =
            (building.cost.toDouble() * (Math.pow(1.15, building.amount.toDouble()))).toInt()
        db.buildingDao().updateById(building.amount, newCost, building.buildingID)

        // Update UI with new buildingCps and clips
        cpsTxt.text = buildingCps.toString()
        clipsTxt.text = clips.toString()
        updateFrags()
    }

    /**
     * updateFrags function: A suspend function to update the list of buildings and the recycler view.
     * Uses coroutines to run the database access in a background thread.
     */
    suspend fun updateFrags() {
        // Load building data from the database and update the buildings list
        val bents = db.buildingDao().getAll()
        buildings.clear()
        for (b in bents) {
            buildings.add(
                Building(
                    b.buildingID,
                    b.img,
                    b.name,
                    b.desc,
                    b.cost,
                    b.cps,
                    b.amount
                )
            )
        }

        // Update the building recycler view adapter and notify data changes
        val recycler = binding.recycleView
        buildingRecyclerAdapter = BuildingRecycleViewAdapter(this, buildings, this)
        recycler.adapter = buildingRecyclerAdapter
        buildingRecyclerAdapter.notifyDataSetChanged()
    }
}