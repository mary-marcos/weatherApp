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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherforecast.data.Repo.Repos
import com.example.weatherforecast.data.Repo.WeatherRemoteSourceImp
import com.example.weatherforecast.data.SharePrefrenceData


import com.example.weatherforecast.databinding.FragmentHomeBinding
import com.example.weatherforecast.model.DailyWeather
import com.example.weatherforecast.model.ForecastWeatherData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {
    val My_LOCATION_PERMISSION_ID = 5005
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
lateinit var viewModel:HomeViewModel
    private var latLng: LatLng? = null
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var dailyAdapter: DailyAdapter
    lateinit var binding: FragmentHomeBinding
    lateinit var sharedpref:SharePrefrenceData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedpref=SharePrefrenceData(requireContext())
    }

    override fun onStart() {
        super.onStart()

        observeLatLng()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeBinding.inflate(inflater,container,false)
        val view =binding.root

        var factory:HomeFactory=HomeFactory(Repos.getInstance(WeatherRemoteSourceImp.getInstance()))

        viewModel = ViewModelProvider(this,factory)[HomeViewModel::class.java]
         observeCurrentweather()

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

        var items = listOf<ForecastWeatherData>()
        hourlyAdapter.submitList(items)

        var items2 = listOf<DailyWeather>()
       dailyAdapter.submitList(items2)

        val networkCallback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Toast.makeText(requireContext(), "Internet back", Toast.LENGTH_SHORT).show()

             //   viewModel.get(44.34, 10.99,"en")

//                lifecycleScope.launch(Dispatchers.IO) {
//                    try {
//                        val apiKey = "fafa4312e27b3dc30f42d0b6d3eccabf"
//                        val response = RetrofitHelper.apiInstance.getCurrentWeather(44.34, 10.99, apiKey, "en")
//
//                        // Check if the response is successful
//                        if (response.isSuccessful) {
//
//                            val weatherData = response.body()
//
//
//                            withContext(Dispatchers.Main) {
//                                Toast.makeText(requireContext(), "response.isSuccessful", Toast.LENGTH_SHORT).show()
//
//                                if (weatherData != null) {
//                                    binding.city.text = weatherData.name
//                                } else {
//                                    Toast.makeText(requireContext(), "No data available", Toast.LENGTH_SHORT).show()
//                                }
//                            }
//                        } else {
//                            withContext(Dispatchers.Main) {
//                                Toast.makeText(requireContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    } catch (e: Exception) {
//
//                        withContext(Dispatchers.Main) {
//                            Toast.makeText(requireContext(), "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                val unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Toast.makeText(requireContext(),"check network",Toast.LENGTH_SHORT).show()
            }
        }

        val connectivityManager1 = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager1.registerDefaultNetworkCallback(networkCallback)

    }
    private fun observeCurrentweather() {
        viewModel.currentWeather.observe(viewLifecycleOwner,Observer{
            binding.weatherDesc.text=it.weather.get(0).description
            binding.windSpeed.text=it.wind.speed.toString()+"m/s"
            binding.temp.text=it.main.temp.toString()
            binding.humidityyy.text=it.main.humidity.toString()+"%"
            binding.pressure.text=it.main.pressure.toString()
            binding.cloudssss.text=it.clouds.all.toString()+"%"
            binding.city.text=it.name
            binding.temp.text=it.main.temp.toString()
            Glide.with(this).load(it.weather.get(0).icon).into(binding.imageView2)
            if (it!=null){ binding.homeProgressBar.visibility= View.GONE}


        }
        )

        viewModel.hourlyForecastWeather.observe(viewLifecycleOwner,Observer{
            var items = it
            hourlyAdapter.submitList(items)

        }
        )

        viewModel.dailyForecastWeather.observe(viewLifecycleOwner,Observer{
            var items = it
            dailyAdapter.submitList(items)

        }
        )

    }


    private fun observeLatLng() {
//        if (sharedpref.getLocationMode()=="GPS"){
//            getLocation()
//        }
//        val savedLatLng = sharedpref.getLatLng()
//        if (savedLatLng != null) {
//            latLng = savedLatLng
//            viewModel.getWeatherData(savedLatLng.latitude, savedLatLng.longitude)
//        }

      var b=  sharedpref.getLocationMode()
   Toast.makeText(requireContext(),b,Toast.LENGTH_SHORT).show()

    if(sharedpref.getLocationMode()=="Map"){
            val savedLatLng = sharedpref.getLatLng()
            if (savedLatLng != null) {
                latLng = savedLatLng
                viewModel.getCurrentWeatherData(savedLatLng.latitude, savedLatLng.longitude)
                viewModel.getForecastWeather(savedLatLng.latitude, savedLatLng.longitude, "en")
        } else{Toast.makeText(requireContext(),"check setting and chose location from map ",Toast.LENGTH_SHORT).show()}
        } else{getLocation()}}



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
        if (requestCode == My_LOCATION_PERMISSION_ID) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext()," ",Toast.LENGTH_SHORT).show()
                getFreshLocation()
              //  requestNewLocation()
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
            LocationRequest.Builder(90000).apply { setPriority(Priority.PRIORITY_HIGH_ACCURACY) }.build(),
            object : LocationCallback() {
                override fun onLocationResult(location: LocationResult) {
                    super.onLocationResult(location)
                    val longit = location.lastLocation?.longitude
                    val latit = location.lastLocation?.latitude
                    if (longit != null && latit != null) {
                        viewModel.getCurrentWeatherData(latit, longit)
                        viewModel.getForecastWeather(latit, longit, "en")
                    }
                }
            }, Looper.getMainLooper()
        )
    }

}
