package com.example.weatherforecast.ui.Alarm

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherforecast.BR
import com.example.weatherforecast.MainActivity
import com.example.weatherforecast.R
import com.example.weatherforecast.data.Repo.LocalSource
import com.example.weatherforecast.data.Repo.Repos
import com.example.weatherforecast.data.Repo.WeatherRemoteSourceImp
import com.example.weatherforecast.databinding.FragmentAlarmBinding
import com.example.weatherforecast.databinding.FragmentAlarmDialogBinding
import com.example.weatherforecast.model.StateManager
import com.example.weatherforecast.model.WeatherAlarm
import kotlinx.coroutines.launch
import java.util.Calendar


class AlarmDialogFragment : DialogFragment() {

    lateinit var viewModel: AlarmViewModel
    lateinit var binding: FragmentAlarmDialogBinding
    lateinit var  weatherAlert:WeatherAlarm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var factory: AlarmFactory =
            AlarmFactory(Repos.getInstance(WeatherRemoteSourceImp.getInstance(), LocalSource.getInstance(requireContext())))

        viewModel = ViewModelProvider(this,factory)[AlarmViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentAlarmDialogBinding.inflate(inflater,container,false)

        val view =binding.root


        binding.btnAddAlert.setOnClickListener {
            val alarmType = if (binding.radioAlarm.isChecked) {
                checkDisplayOverOtherAppPerm()
                "Alarm"
            } else {
                if (!isNotificationServiceEnabled()) {
                    openNotificationAccessSettings()
                }
                "Notification"
            }

            val hour = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                binding.timePickerDuration.hour
            } else {
                binding.timePickerDuration.currentHour
            }
            val minute = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                binding.timePickerDuration.minute
            } else {
                binding.timePickerDuration.currentMinute
            }
            val durationMinutes = getDurationInMilliseconds(hour, minute)
            val isActive = binding.switchAlertEnabled.isChecked
             weatherAlert = WeatherAlarm(
                hour = hour,
                minute = minute,
               duration = durationMinutes,
                type = alarmType,
                isActive = isActive
            )

            obseveInstallingAlarm()

            viewModel.addWeatherAlert(weatherAlert, requireContext())

         //   Toast.makeText(requireContext(), "Weather alert added", Toast.LENGTH_SHORT).show()
         //  dismiss()
        }
        return view   }

    private fun obseveInstallingAlarm() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.inserAlarm.collect { state: StateManager<Long> ->
                when (state) {
                    is StateManager.Loading -> {
                         Toast.makeText(requireContext(), "loading", Toast.LENGTH_SHORT).show()

                      //  binding.homeProgressBar.visibility=View.VISIBLE
                    }
                    is StateManager.Success -> {


                        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

                         val alarmIntent = Intent( context, MyAlarmReceiver::class.java  )
                                    alarmIntent.putExtra(
                MyAlarmReceiver.ALERT_TYPE_EXTRA,
                 weatherAlert.type
            )
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                weatherAlert.duration.toInt(),
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, weatherAlert.hour)
            calendar.set(Calendar.MINUTE, weatherAlert.minute)
            calendar.set(Calendar.SECOND, 0)
            val alarmTime = calendar.timeInMillis

            Log.d("WeatherAlertsViewModel", "Alarm time: $alarmTime")

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        alarmTime,
                        pendingIntent
                    )
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        alarmTime,
                        pendingIntent
                    )
                } else {
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        alarmTime,
                        pendingIntent
                    )

                }
                Toast.makeText(requireContext(),"Alarm Inserted Successfully",Toast.LENGTH_SHORT).show()

                dismiss()
            } catch (e: SecurityException) {
                Log.e("WeatherAlertsViewModel", "SecurityException: ${e.message}")
                // Handle the SecurityException here
            }


                    }
                    is StateManager.Error -> {
                      //  binding.homeProgressBar.visibility= View.GONE
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }


                    else -> {}
                }
            }
        }
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val packageName = requireActivity().packageName
        val flat = Settings.Secure.getString(requireActivity().contentResolver, "enabled_notification_listeners")
        return flat != null && flat.contains(packageName)
    }

    private fun checkDisplayOverOtherAppPerm() {
        if (!Settings.canDrawOverlays(requireActivity())) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + requireActivity().packageName)
            )
            someActivityResultLauncher.launch(intent)
        }
    }
    private val someActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (!Settings.canDrawOverlays(requireContext())) {

            }
        }


    private fun openNotificationAccessSettings() {
        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        startActivity(intent)
    }
    private fun getDurationInMilliseconds(hour: Int, minute: Int): Long {
        return (hour * 60 * 60 * 1000 + minute * 60 * 1000).toLong()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MainActivity.REQUEST_CODE_NOTIFICATION_ACCESS) {
            if (resultCode == Activity.RESULT_OK) {
            } else {
            }
        }
    }
}