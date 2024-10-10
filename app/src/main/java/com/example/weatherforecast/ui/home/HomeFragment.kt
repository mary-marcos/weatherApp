package com.example.weatherforecast.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherforecast.data.Repo.LocalSource
import com.example.weatherforecast.data.Repo.Repos
import com.example.weatherforecast.data.Repo.WeatherRemoteSourceImp
import com.example.weatherforecast.data.SharePrefrenceData


import com.example.weatherforecast.databinding.FragmentHomeBinding
import com.example.weatherforecast.model.DailyWeather
import com.example.weatherforecast.model.ForecastWeatherData
import com.example.weatherforecast.model.HourlyWeather
import com.example.weatherforecast.model.StateManager
import com.example.weatherforecast.NetworkChangeListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import kotlinx.coroutines.launch
import com.example.weatherforecast.model.getweatherIconResourceId


class HomeFragment : Fragment() {

    val My_LOCATION_PERMISSION_ID = 5005
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
lateinit var viewModel:HomeViewModel
    private var latLng: LatLng? = null
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var dailyAdapter: DailyAdapter
    lateinit var binding: FragmentHomeBinding
    lateinit var sharedpref:SharePrefrenceData
   // private lateinit var networkChangeListener: NetworkChangeListener



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedpref=SharePrefrenceData(requireContext())



    }

    override fun onResume() {
        super.onResume()
        checkfromisFv()
    }

    override fun onStart() {
        super.onStart()
        checkfromisFv()
     //   networkChangeListener.register()

    }

    override fun onDestroy() {
        super.onDestroy()
      //  networkChangeListener.unregister()
    }
    override fun onStop() {
        super.onStop()
      //  networkChangeListener.unregister()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeBinding.inflate(inflater,container,false)
        val view =binding.root

        var factory:HomeFactory=HomeFactory(Repos.getInstance(WeatherRemoteSourceImp.getInstance(), LocalSource.getInstance(requireContext())))

        viewModel = ViewModelProvider(this,factory)[HomeViewModel::class.java]

            return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hourlyAdapter = HourlyAdapter()
        dailyAdapter = DailyAdapter()

        binding.hourlyrecyclerView
       .apply {
            adapter=hourlyAdapter
           // layoutManager = LinearLayoutManager(context)
        }
        binding.dailyRecyclerView
            .apply {
                adapter=dailyAdapter
                 layoutManager = LinearLayoutManager(context)
            }

        var items = listOf<HourlyWeather>()
        hourlyAdapter.submitList(items)

        var items2 = listOf<DailyWeather>()
       dailyAdapter.submitList(items2)


        observeData()

        checkfromisFv()

    }


    fun observeData(){

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Collect current weather data
                launch {  viewModel.dailyForecastWeather.collect { result ->
                    when (result) {
                        is StateManager.Success -> {
                            val dailyData = result.data
                            dailyAdapter.submitList(dailyData)
                        }
                        is StateManager.Loading -> {
                            //   Toast.makeText(requireContext(), "Loading daily forecast...", Toast.LENGTH_SHORT).show()
                        }
                        is StateManager.Error -> {
                            val errorMessage = result.message ?: "Failed to load daily forecast"
                            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                } }


                launch { viewModel.currentWeather.collect { result ->
                    when (result) {
                        is StateManager.Success -> {
                            val weatherData = result.data

                            // Bind data to UI
                            binding.weatherDesc.text = weatherData.description
                            binding.windSpeed.text = weatherData.windSpeed
                            binding.temp.text = weatherData.temp
                            binding.humidityyy.text = "${weatherData.humidity}%"
                            binding.pressure.text = weatherData.pressure
                            binding.cloudssss.text = "${weatherData.clouds}%"
                            binding.city.text = weatherData.city
                            binding.temppp.text = weatherData.temp
                           // Glide.with(requireActivity()).load(weatherData.iconCode).into(binding.imageView2)

                            weatherData.iconCode?.let { getweatherIconResourceId(it) }
                                ?.let { binding.imageView2.setImageResource(it) }
                            // Hide progress bar
                            binding.homeProgressBar.visibility = View.GONE

                        }
                        is StateManager.Loading -> {
                            binding.homeProgressBar.visibility = View.VISIBLE
                            //   Toast.makeText(requireContext(), "Loading current weather...", Toast.LENGTH_SHORT).show()
                        }
                        is StateManager.Error -> {
                            binding.homeProgressBar.visibility = View.GONE
                            val errorMessage = result.message ?: "An error occurred"
                            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                }}


                launch {  viewModel.hourlyForecastWeather.collect { result ->
                    when (result) {
                        is StateManager.Success -> {
                            val hourlyData = result.data
                            if (hourlyData != null) {

                                hourlyAdapter.submitList(hourlyData)
                            }
                        }
                        is StateManager.Loading -> {
                            Toast.makeText(requireContext(), "Loading hourly forecast...", Toast.LENGTH_SHORT).show()
                        }
                        is StateManager.Error -> {
                            val errorMessage = result.message ?: "Failed to load hourly forecast"
                            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                        }

                    }
                } }


                // Collect hourly forecast data

            }
        }
    }

    private fun checkfromisFv() {
        val args = HomeFragmentArgs.fromBundle(requireArguments())
        if(args!=null){
            var isfav=args.fromFav.split(",")
            if (isfav[0]=="fav") {
                var lat =isfav[1].toDouble()
                var lang=isfav[2].toDouble()
                viewModel.getCurrentWeatherData(lat, lang,requireContext())
                viewModel.getForecastWeather(lat, lang,requireContext())


            }else{
                observeLatLng()
            }
        }

    }
    private fun observeLatLng() {

//      var b=  sharedpref.getLocationMode()
//   Toast.makeText(requireContext(),b,Toast.LENGTH_SHORT).show()

    if(sharedpref.getLocationMode()=="Map"){
            val savedLatLng = sharedpref.getLatLng()
            if (savedLatLng != null) {
                latLng = savedLatLng
                viewModel.getCurrentWeatherData(savedLatLng.latitude, savedLatLng.longitude,requireContext())
                viewModel.getForecastWeather(savedLatLng.latitude, savedLatLng.longitude,requireContext())

        } else{Toast.makeText(requireContext(),"check setting and chose location from map ",Toast.LENGTH_SHORT).show()}
        }

    else{getLocation()}}



    //////////////////init gps//////////////

    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                getFreshLocation()
               // requestNewLocation()
            } else {
                enableLocationServices()
//                Toast.makeText(requireContext(), "Please, Turn on location", Toast.LENGTH_LONG).show()
//                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }
    fun enableLocationServices() {
        Toast.makeText(requireContext(),"please turn on the Location",Toast.LENGTH_SHORT).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == My_LOCATION_PERMISSION_ID ) {
            if ( grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getFreshLocation()
            }
        }
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ), My_LOCATION_PERMISSION_ID
        )



    }

    @SuppressLint("MissingPermission")
    fun getFreshLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.Builder(180000).apply { setPriority(Priority.PRIORITY_HIGH_ACCURACY) }.build(),
            object : LocationCallback() {
                override fun onLocationResult(location: LocationResult) {
                    super.onLocationResult(location)
                    val longit = location.lastLocation?.longitude
                    val latit = location.lastLocation?.latitude
                    if (longit != null && latit != null) {
                        viewModel.getCurrentWeatherData(latit, longit,requireContext())
                        viewModel.getForecastWeather(latit, longit, requireContext())
                    }
                }
            }, Looper.getMainLooper()
        )
    }

}
