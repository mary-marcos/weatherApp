package com.example.weatherforecast.home

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.weatherforecast.Api.RetrofitHelper
import com.example.weatherforecast.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentHomeBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment

        val view =binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val networkCallback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Toast.makeText(requireContext(), "Internet back", Toast.LENGTH_SHORT).show()

                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val apiKey = "fafa4312e27b3dc30f42d0b6d3eccabf"
                        val response = RetrofitHelper.apiInstance.getCurrentWeather(44.34, 10.99, apiKey, "en")

                        // Check if the response is successful
                        if (response.isSuccessful) {

                            val weatherData = response.body()


                            withContext(Dispatchers.Main) {
                                Toast.makeText(requireContext(), "response.isSuccessful", Toast.LENGTH_SHORT).show()

                                if (weatherData != null) {
                                    binding.city.text = weatherData.name
                                } else {
                                    Toast.makeText(requireContext(), "No data available", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(requireContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {

                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
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
}