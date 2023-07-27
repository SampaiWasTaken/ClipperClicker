package com.example.appdevprojectradlerprueller

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import java.math.BigInteger

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * BuildingFragment is a simple Fragment subclass that displays building information and handles
 * user interactions related to buildings in the app.
 */
class BuildingFragment : Fragment() {
    // Parameters to be passed to the fragment (not used in the current implementation)
    private var param1: String? = null
    private var param2: String? = null

    // Listener to handle the buy button press event in the parent activity or fragment
    private var buyBtnListener: BuyBtnListener? = null

    /**
     * The interface that any activity or fragment hosting this BuildingFragment should implement
     * to receive the buy button press events.
     */
    interface BuyBtnListener {
        /**
         * A suspend function to handle the buy button press event.
         *
         * @param building The Building object representing the building being bought.
         */
        suspend fun buyBtnPressed(building: Building)
    }

    /**
     * Called when the fragment is attached to the activity.
     * Tries to set the buyBtnListener if the hosting context implements BuyBtnListener interface.
     *
     * @param context The context to attach the fragment to.
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            buyBtnListener = context as BuyBtnListener
            Log.e("Fragments", "shit Attached")
        } catch (e: ClassCastException) {
            Log.e(
                "Fragments",
                "${context.toString()} must implement BuyBtnListener u fucking retard"
            )
        }
    }

    /**
     * Called when the fragment is created.
     *
     * @param savedInstanceState The saved instance state of the fragment.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    /**
     * Called when the fragment's view is being created.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views.
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState The saved instance state of the fragment's view.
     * @return The View for the fragment's UI.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_building, container, false)

        // Disable bitmap filtering for certain drawables to maintain a crisp appearance
        view.findViewById<ImageView>(R.id.buildingFrame).drawable.isFilterBitmap = false
        view.findViewById<ImageView>(R.id.buildingIcon).drawable.isFilterBitmap = false
        view.findViewById<AppCompatButton>(R.id.imageButton).background.isFilterBitmap = false
        return view

    }

    companion object {
        /**
         * Use this factory method to create a new instance of the BuildingFragment using the
         * provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of BuildingFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BuildingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    /**
     * A function to retrieve the current value of the "clips" variable.
     * This function assumes that the variable "clips" is accessible in the parent scope.
     * Make sure to declare "clips" variable appropriately before calling this function.
     *
     * @return The current value of the "clips" variable as a BigInteger.
     */
    fun getClips(): BigInteger {
        return clips
    }
}