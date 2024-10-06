package com.example.weatherforecast

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.preference.PreferenceManager
import android.util.Log
import com.example.weatherforecast.data.SharePrefrenceData
import java.util.Locale

object LocaleHelper {




//    private fun setLocale(languageCode: String,context: Context) {
//        val locale = Locale(languageCode)
//        Locale.setDefault(locale)
//        val resources: Resources = context.resources
//        val configuration: Configuration = resources.configuration
//        configuration.setLocale(locale)
//        resources.updateConfiguration(configuration, resources.displayMetrics)
//    }

//    fun setDefaultLanguage(preferenceManager: PreferenceManager,context: Context) {
//        val languageCode = preferenceManager.checkLanguage()
//        val defaultLanguageCode = if (languageCode == "en") "en" else "ar"
//        setLocale(defaultLanguageCode, context)
//    }

    fun setLocale( sharePrefrenceData: SharePrefrenceData,context: Context, language: String): Context {
      var reso=  updateResources(context, language)
        sharePrefrenceData.saveLanguagePreference(context, language)
        //saveLanguagePreference(context, language)
        return reso
    }
    fun setdefault(   sharePrefrenceData: SharePrefrenceData,context: Context): Context {
        var language =sharePrefrenceData.getSavedLanguage()?:"en"
        var reso=  updateResources(context, language)
        sharePrefrenceData.
        saveLanguagePreference(context, language)
        return reso
    }





    private fun updateResources(context: Context, language: String): Context {
//        val locale = Locale(language)
//        Locale.setDefault(locale)
//
//        val config = Configuration()
//        config.setLocale(locale)
//        config.setLayoutDirection(locale)



        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources: Resources = context.resources
        val configuration: Configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context.createConfigurationContext(configuration)
    }

    fun getCurrentLocale(resources: Resources): Locale {
        return resources.configuration.locales[0]
    }
}