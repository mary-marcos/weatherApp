package com.example.weatherforecast.ui.map

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherforecast.R
import com.example.weatherforecast.data.Repo.LocalSource
import com.example.weatherforecast.data.Repo.Repos
import com.example.weatherforecast.data.Repo.WeatherRemoteSourceImp
import com.example.weatherforecast.data.SharePrefrenceData
import com.example.weatherforecast.databinding.FragmentHomeBinding
import com.example.weatherforecast.databinding.FragmentMyMapBinding
import com.example.weatherforecast.databinding.FragmentSettingBinding
import com.example.weatherforecast.model.FavItem
import com.example.weatherforecast.model.StateManager
import com.example.weatherforecast.ui.home.HomeFactory
import com.example.weatherforecast.ui.home.HomeViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch


class MyMapFragment : Fragment(),OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    lateinit var binding: FragmentMyMapBinding
    lateinit var viewModel:MapViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var factory: FavFactory =
           FavFactory(Repos.getInstance(WeatherRemoteSourceImp.getInstance(), LocalSource.getInstance(requireContext())))

        viewModel = ViewModelProvider(this,factory)[MapViewModel::class.java]

        binding= FragmentMyMapBinding.inflate(inflater,container,false)

        val view =binding.root
        return view
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        checkVisibility()



        binding.saveToShared.setOnClickListener {
            getSelectedLocation()

        }
        binding.saveToFav.setOnClickListener { saveSelectedLocationToFav() }
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



    private fun saveSelectedLocationToFav() {
        if (::mMap.isInitialized) {
            val selectedLocation = mMap.cameraPosition.target


            observeFavData()
            viewModel.saveLocationToFav(selectedLocation,requireContext())
        }
    }

    private fun getSelectedLocation() {
        if (::mMap.isInitialized) {
            val selectedLocation = mMap.cameraPosition.target
            val PreferenceManager=SharePrefrenceData(requireContext())
            PreferenceManager.saveLatLng(selectedLocation)
            requireActivity().supportFragmentManager.popBackStack()


        } else {
            Log.e("TAG", "Google Map is not initialized")
        }
    }

    private fun checkVisibility() {
        val args = MyMapFragmentArgs.fromBundle(requireArguments())
       var isfav=args.isfav
        if (isfav=="fav") {
           binding.saveToFav.visibility = View.VISIBLE
           binding.saveToShared.visibility = View.GONE
        }
    }

    private fun observeFavData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.favLocation.collect { state: StateManager<Long> ->
                when (state) {
                    is StateManager.Loading -> {
                     //   Toast.makeText(requireContext(), state.toString(), Toast.LENGTH_SHORT).show()

                        binding.homeProgressBar.visibility=View.VISIBLE
                    }
                    is StateManager.Success -> {

                      //  Toast.makeText(requireContext(), state.toString(), Toast.LENGTH_SHORT).show()
                        binding.homeProgressBar.visibility= View.GONE
                      Toast.makeText(requireContext(),"Location Inserted Successfully",Toast.LENGTH_SHORT).show()
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                    is StateManager.Error -> {
                        binding.homeProgressBar.visibility= View.GONE
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }


                    else -> {}
                }
            }
        }
    }

}