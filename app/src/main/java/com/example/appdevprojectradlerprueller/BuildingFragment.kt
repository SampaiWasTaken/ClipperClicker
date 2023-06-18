package com.example.appdevprojectradlerprueller

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import java.math.BigInteger

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BuildingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BuildingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var buyBtnListener: BuyBtnListener? = null
    interface BuyBtnListener
    {
        suspend fun buyBtnPressed(building: Building)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            buyBtnListener = context as BuyBtnListener

            Log.e("Fragments","shit Attached")
        } catch (e: ClassCastException) {
            Log.e("Fragments","${context.toString()} must implement BuyBtnListener u fucking retard")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_building, container, false)
        view.findViewById<ImageView>(R.id.buildingFrame).drawable.isFilterBitmap = false
        view.findViewById<ImageView>(R.id.buildingIcon).drawable.isFilterBitmap = false
        view.findViewById<AppCompatButton>(R.id.imageButton).background.isFilterBitmap = false
        return view

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BuildingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    fun getClips(): BigInteger
    {
        return clips
    }
}