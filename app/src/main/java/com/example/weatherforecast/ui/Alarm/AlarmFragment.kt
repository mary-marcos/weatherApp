package com.example.weatherforecast.ui.Alarm

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.R
import com.example.weatherforecast.data.Repo.LocalSource
import com.example.weatherforecast.data.Repo.Repos
import com.example.weatherforecast.data.Repo.WeatherRemoteSourceImp
import com.example.weatherforecast.databinding.AlarmItemBinding
import com.example.weatherforecast.databinding.FragmentAlarmBinding
import com.example.weatherforecast.databinding.FragmentFavBinding
import com.example.weatherforecast.model.FavItem
import com.example.weatherforecast.model.StateManager
import com.example.weatherforecast.model.WeatherAlarm
import kotlinx.coroutines.launch


class AlarmFragment : Fragment(),OnAlarmClicked {
    lateinit var viewModel: AlarmViewModel
    //  private var latLng: LatLng? = null

    lateinit var alarmAdapter: AlarmAdapter
    lateinit var binding: FragmentAlarmBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var factory: AlarmFactory =
            AlarmFactory(Repos.getInstance(WeatherRemoteSourceImp.getInstance(), LocalSource.getInstance(requireContext())))

        viewModel = ViewModelProvider(this,factory)[AlarmViewModel::class.java]

        binding= FragmentAlarmBinding.inflate(inflater,container,false)

        val view =binding.root
        return view //inflater.inflate(R.layout.fragment_fav, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createNotificationChannel()
        binding.fab.setOnClickListener {
            val addDialog = AlarmDialogFragment()
            addDialog.show(parentFragmentManager, "AddWeatherAlert")
        }
        alarmAdapter=AlarmAdapter(this)
        binding.alarmRecyclerView .apply {
            adapter=alarmAdapter
            layoutManager = LinearLayoutManager(context)
        }



       observeAlarmData()
        viewModel.getAlrmss()

    }
    private fun observeAlarmData() {
        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.getAlarms.collect { state: StateManager<List<WeatherAlarm>?> ->
                when (state) {
                    is StateManager.Loading -> {
                        binding.alarmProgressBar.visibility=View.VISIBLE
                    }
                    is StateManager.Success -> {
                        binding.alarmProgressBar.visibility= View.GONE
                        alarmAdapter.submitList(state.data)
                    }
                    is StateManager.Error -> {
                        binding.alarmProgressBar.visibility= View.GONE
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }


                    else -> {}
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "weather_alerts"
            val channelName = "Weather Alerts"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = "Channel for Weather Alerts"
            }

            val notificationManager = requireContext().getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }


    override fun respondalarm(favItem: WeatherAlarm) {

    }

    private fun observedeleteData() {
        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.deleteAlarm.collect { state: StateManager<Int?> ->
                when (state) {
                    is StateManager.Loading -> {
                        binding.alarmProgressBar.visibility=View.VISIBLE
                    }
                    is StateManager.Success -> {

                        binding.alarmProgressBar.visibility= View.GONE
                        Toast.makeText(requireContext(), "Item {${state.data}}deleted success", Toast.LENGTH_SHORT).show()

                    }
                    is StateManager.Error -> {
                        binding.alarmProgressBar.visibility= View.GONE
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }


                    else -> {}
                }
            }
        }
    }

    override fun deletitemalarm(alarmItem: WeatherAlarm) {
        AlertDialog.Builder(context)
            .setTitle("Delete Item")
            .setMessage("Are you sure you want to delete this item?")
            .setPositiveButton("Yes") { dialog, which ->
                // User clicked Yes button
                observedeleteData()
                viewModel.deleteAlarm(alarmItem)
                cancelAlarm(alarmItem)

                // Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No") { dialog, which ->

                dialog.dismiss()
            }
            .show()
    }


    private fun cancelAlarm(weatherAlert: WeatherAlarm) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val alarmIntent = Intent(context, MyAlarmReceiver::class.java)
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

        alarmManager.cancel(pendingIntent)
        Toast.makeText(requireContext(), "Alarm Cancelled", Toast.LENGTH_SHORT).show()
    }

    }

