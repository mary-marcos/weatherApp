package com.example.weatherforecast.ui.Alarm

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.location.Location
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.R
import com.example.weatherforecast.data.Repo.LocalSource
import com.example.weatherforecast.data.Repo.Repos
import com.example.weatherforecast.data.Repo.WeatherRemoteSourceImp
import com.example.weatherforecast.data.SharePrefrenceData
import com.example.weatherforecast.model.StateManager
import com.example.weatherforecast.ui.home.HomeFactory
import com.example.weatherforecast.ui.home.HomeViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

class MyAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_DELETE -> {
                Log.d(TAG, "Notification dismissed")
                stopAlarmSound(context)
            }
            else -> {
                fetchWeatherDescriptionAndShowNotification(context,intent)

            }
        }
    }



//
//fun getDescrip(context: Context){
//    var factory: AlarmFactory =
//        AlarmFactory(Repos.getInstance(WeatherRemoteSourceImp.getInstance(), LocalSource.getInstance(requireContext())))
//
//  var  viewModel = ViewModelProvider(this,factory)[HomeViewModel::class.java]
//
//    var sharePrefrenceData= SharePrefrenceData(context)
//    var lan= sharePrefrenceData.getLatLng()
//                if(sharePrefrenceData.getLocationMode()=="Map"){
//                val savedLatLng = sharePrefrenceData.getLatLng()
//                    //latLng = savedLatLng
//                    viewModel.getCurrentWeatherData(savedLatLng.latitude, savedLatLng.longitude,lan)
//
//                }else{
//
//            }
//}



    private fun fetchWeatherDescriptionAndShowNotification(context: Context, intent: Intent) {
        val repo = Repos.getInstance(WeatherRemoteSourceImp.getInstance(), LocalSource.getInstance(context))

        CoroutineScope(Dispatchers.IO).launch {
            var lat: Double
            var long: Double
            val sharePrefrenceData = SharePrefrenceData(context)


            val latLng = sharePrefrenceData.getLatLng()
            if (sharePrefrenceData.getLocationMode() == "Map" && latLng != null) {
                lat = latLng.latitude
                long = latLng.longitude
            } else {
                val freshLocation = getFreshLocation(context)
                if (freshLocation != null) {
                    lat = freshLocation.latitude
                    long = freshLocation.longitude
                } else {

                    Log.e("LocationError", "Location could not be fetched")
                    val alertType = intent.getStringExtra(ALERT_TYPE_EXTRA)
                    Log.d(TAG, "fetchWeatherDescriptionAndShowNotification:$alertType ")
                    val notification = buildNotification(alertType ?: "Notification", "Location not available", context)
                    showNotification(context, notification)
                    return@launch  // Exit the coroutine early if location is not available
                }
            }

            val lang = sharePrefrenceData.getSavedLanguage() ?: "en"


            repo.getCurrentWeather(lat, long, lang)
                .catch { exception ->

                    Log.e("WeatherError", "Error fetching weather data", exception)
                }
                .collect { data ->

                    val description = data.weather[0].description

                    val alertType = intent.getStringExtra(ALERT_TYPE_EXTRA)
                    if (alertType=="Notification"){
                        val notification = buildNotification(alertType , description, context)
                        showNotification(context, notification)
                    }
                    else{

                        createAlarm(context,description)
                    }

                }
        }
    }


    @SuppressLint("MissingPermission")
    suspend fun getFreshLocation(context: Context): Location? = suspendCancellableCoroutine { continuation ->
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        val locationRequest = LocationRequest.Builder(90000)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    fusedLocationProviderClient.removeLocationUpdates(this)
                    val location = locationResult.lastLocation
                    if (location != null) {
                        continuation.resume(location) {}
                    } else {
                        continuation.resume(null) {}
                    }
                }
            },
            Looper.getMainLooper()
        )
    }

    private fun buildNotification(alertType: String, descrip:String, context: Context): NotificationCompat.Builder {
        val channelId = "weather_alerts"

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.baseline_notifications_active_24)
            .setContentTitle("Weather Alert")
            .setContentText(descrip)
            .setAutoCancel(true)

        val soundUri = when (alertType) {
            "Alarm" -> RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            else -> RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }
        notificationBuilder.setSound(soundUri)

        return notificationBuilder
    }

    private fun showNotification(context: Context, notificationBuilder: NotificationCompat.Builder) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        val notification = notificationBuilder.build()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private suspend fun createAlarm(context: Context, message: String) {
        val mediaPlayer = MediaPlayer.create(context, R.raw.alarm)

        val view: View = LayoutInflater.from(context).inflate(R.layout.alertalarm, null, false)
        val dismissBtn = view.findViewById<Button>(R.id.button_dismiss)
        val textView = view.findViewById<TextView>(R.id.textViewMessage)
        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            LAYOUT_FLAG,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        layoutParams.gravity = Gravity.CENTER


        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        withContext(Dispatchers.Main) {
            windowManager.addView(view, layoutParams)
            view.visibility = View.VISIBLE
            textView.text = message
        }

        mediaPlayer.start()
        mediaPlayer.isLooping = true
        dismissBtn.setOnClickListener {
            mediaPlayer?.release()
            windowManager.removeView(view)
        }

    }

    private fun playAlarmSound(context: Context) {
        val alarmSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val ringtone = RingtoneManager.getRingtone(context, alarmSoundUri)
        ringtone.play()
    }

    private fun stopAlarmSound(context: Context) {
        val alarmSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val ringtone = RingtoneManager.getRingtone(context, alarmSoundUri)
        ringtone.stop()
    }

    companion object {
        val LAYOUT_FLAG =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else WindowManager.LayoutParams.TYPE_PHONE

        private const val TAG = "AlarmReceiver"
        const val NOTIFICATION_ID = 1234
        const val ALERT_TYPE_EXTRA = "alert_type"
        fun stopAlarmSound(context: Context) {
            val alarmSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            val ringtone = RingtoneManager.getRingtone(context, alarmSoundUri)
            ringtone.stop()
        }
    }

}