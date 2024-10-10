package com.example.weatherforecast.ui.Alarm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.AlarmItemBinding

import com.example.weatherforecast.model.WeatherAlarm
import com.example.weatherforecast.ui.favorate.onFavClicked


class AlarmAdapter(var oncalarmlicked: OnAlarmClicked) : ListAdapter<WeatherAlarm, AlarmAdapter.ItemViewHolder>(DiffCallback()) {
    lateinit var binding: AlarmItemBinding

    class ItemViewHolder(var binding: AlarmItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
//databinding
        val inflater = LayoutInflater.from(parent.context)


        binding = DataBindingUtil.inflate(inflater, R.layout.alarm_item, parent, false)

        // binding=ProductItemBinding.inflate(inflater,parent,false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmAdapter.ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.binding.weatherAlarm = current
        holder.binding.deleteIcon.setOnClickListener { oncalarmlicked.deletitemalarm(current) }


    }
}


class DiffCallback : DiffUtil.ItemCallback<WeatherAlarm>() {
    override fun areItemsTheSame(oldItem: WeatherAlarm, newItem: WeatherAlarm): Boolean {
        return oldItem.duration == newItem.duration
    }

    override fun areContentsTheSame(oldItem: WeatherAlarm, newItem: WeatherAlarm): Boolean {
        return oldItem.duration == newItem.duration
    }
}