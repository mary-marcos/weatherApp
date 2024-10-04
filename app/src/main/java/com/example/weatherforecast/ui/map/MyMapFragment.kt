package com.example.weatherforecast.ui.map

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherforecast.R
import com.example.weatherforecast.data.SharePrefrenceData
import com.example.weatherforecast.databinding.FragmentHomeBinding
import com.example.weatherforecast.databinding.FragmentMyMapBinding
import com.example.weatherforecast.databinding.FragmentSettingBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MyMapFragment : Fragment(),OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    lateinit var binding: FragmentMyMapBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentMyMapBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment

        val view =binding.root
        return view
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
//        val mapFragment = SupportMapFragment.newInstance()
//        childFragmentManager.beginTransaction()
//            .replace(R.id.map_container, mapFragment)
//            .commit()
//
//        mapFragment.getMapAsync(this)
        binding.saveToShared.setOnClickListener {
            getSelectedLocation()

        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Set a default location and add a marker
        val exampleLocation = LatLng(-34.0, 151.0) // Replace with your desired coordinates
        mMap.addMarker(MarkerOptions().position(exampleLocation).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(exampleLocation, 10f)) // Zoom to level 10

      //  googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))

        mMap.setOnMapClickListener { latLng ->
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(latLng))
        }
    }

    private fun getSelectedLocation() {
        if (::mMap.isInitialized) {
            val selectedLocation = mMap.cameraPosition.target
            val PreferenceManager=SharePrefrenceData(requireContext())
            PreferenceManager.saveLatLng(selectedLocation)
            requireActivity().supportFragmentManager.popBackStack()

            //  navigateToHomeFragment(selectedLocation)
        } else {
            Log.e("TAG", "Google Map is not initialized")
        }
    }

//    private fun checkButtonsVisibility() {
//        val isFavBtnVisible = MapSelectionFragmentArgs.fromBundle(requireArguments()).isFavVisible
//        if (isFavBtnVisible) {
//            viewDataBinding.saveToFavBtn.visibility = View.VISIBLE
//            viewDataBinding.selectLocationButton.visibility = View.GONE
//        }
//    }

}