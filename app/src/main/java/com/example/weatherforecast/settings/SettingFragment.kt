package com.example.weatherforecast.settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherforecast.LocaleHelper
import com.example.weatherforecast.MainActivity
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {
    lateinit var binding: FragmentSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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

        super.onViewCreated(view, savedInstanceState)
        binding.languageGroup
        binding.radioButtonAr.setOnClickListener {
            switchLanguage("ar")
        }
        binding.radioButtonEng.setOnClickListener {
             switchLanguage("en")
        }
    }


    private fun switchLanguage(language: String) {
        // Change the locale and restart the activity to apply the changes
        LocaleHelper.setLocale(requireContext(), language)

//        requireActivity().supportFragmentManager.beginTransaction()
//            .replace(R.id.frameLayout3, SettingFragment())
//            .commit()


        requireActivity().finish()
        val intent = Intent (requireContext(), MainActivity::class.java)
        startActivity(intent)



//        val intent = Intent(requireContext(), MainActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
//        startActivity(intent)
//        requireActivity().finish()// Close the current activity
    }
    private fun refreshMainActivity() {
        requireActivity().finish()
        val intent = Intent (requireContext(), MainActivity::class.java)
        startActivity(intent)
    }

}