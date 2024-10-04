package com.example.weatherforecast.ui.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.weatherforecast.LocaleHelper
import com.example.weatherforecast.MainActivity
import com.example.weatherforecast.R
import com.example.weatherforecast.data.SharePrefrenceData
import com.example.weatherforecast.databinding.FragmentSettingBinding



class SettingFragment : Fragment() {
    lateinit var binding: FragmentSettingBinding
    lateinit var sharedPrefrenceData: SharePrefrenceData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPrefrenceData=  SharePrefrenceData(requireContext())
        binding= FragmentSettingBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment

        val view =binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val currentLocale = LocaleHelper.getCurrentLocale(resources).language
        if (currentLocale == "ar") {
            binding.radioButtonAr.isChecked = true
        } else {
            binding.radioButtonEng.isChecked = true
        }
        updateLocationModeRadioButton()
        updateTempUnitModeRadioButton()


        super.onViewCreated(view, savedInstanceState)

        setListener()

    }

    private fun setListener(){
        binding.languageGroup
        binding.radioButtonAr.setOnClickListener {
            switchLanguage("ar")
        }
        binding.radioButtonEng.setOnClickListener {
            switchLanguage("en")
        }
////
        binding.locationGroup
        binding.radioButtonGps.setOnClickListener {
            sharedPrefrenceData.saveLocationMode("GPS")

        }
        binding.radioButtonMap.setOnClickListener {
            sharedPrefrenceData.saveLocationMode("Map")

            Toast.makeText(requireContext(),"navvvv",Toast.LENGTH_SHORT).show()
            Log.d("TAG", "navigate : ")
            navigateToMapSelectionFragment()
        }
/////////
        binding.tempGroup
        binding.radioButtonClis.setOnClickListener {
            switchTemp("Celsius")
        }
        binding.radioButtonKilven.setOnClickListener {
            switchTemp("KELVIN")
        }
        binding.radioButtonFehren.setOnClickListener {
            switchTemp("Fahrenheit")
        }
    }

    private fun updateLocationModeRadioButton() {
        when (sharedPrefrenceData.getLocationMode()) {
            "GPS" -> binding.radioButtonGps.isChecked = true
            "Map" -> binding.radioButtonMap.isChecked = true
        }
    }

    private fun updateTempUnitModeRadioButton() {
        when (sharedPrefrenceData.getTempunit()) {
            "Celsius" -> binding.radioButtonClis.isChecked = true
            "KELVIN" -> binding.radioButtonKilven.isChecked = true
            "Fahrenheit" -> binding.radioButtonFehren.isChecked = true
        }
    }

    private fun switchTemp(temp: String) {
        sharedPrefrenceData.setTempunit(temp)
        refreshMainActivity()

    }

    private fun switchLanguage(language: String) {

        LocaleHelper.setLocale(sharedPrefrenceData,requireContext(), language)
        refreshMainActivity()

    }
    private fun refreshMainActivity() {
        requireActivity().finish()
        val intent = Intent (requireContext(), MainActivity::class.java)
        startActivity(intent)
    }

    fun navigateToMapSelectionFragment() {
        findNavController().navigate(R.id.action_settingFragment_to_myMapFragment)
     //   findNavController().navigate(R.id.action_settingFragment_to_mapFragment)
    }

}