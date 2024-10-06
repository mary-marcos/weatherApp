package com.example.weatherforecast.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecast.databinding.ItemHourlyBinding
import com.example.weatherforecast.model.ForecastWeatherData
import com.example.weatherforecast.model.HourlyWeather

class HourlyAdapter():  ListAdapter<HourlyWeather, HourlyAdapter.ItemViewHolder>(DiffCallback())  {
    lateinit var binding: ItemHourlyBinding
    class ItemViewHolder(var binding: ItemHourlyBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding=ItemHourlyBinding.inflate(inflater,parent,false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current=getItem(position)
        holder.binding.tvTime.text=current.hour
      //  tittleTv.text=current.title
        holder.binding.tvTemperature.text=current.Temp

        Glide.with(holder.itemView.context).load(current.iconImg).into(holder.binding.IconView)
//        holder.binding.
//        cardView.setOnClickListener { listen.listener(current)
        }
        // mylistener.invoke(current)

        //  tittle.text=current.title
        // holder.description.text=current.descriptiocn
        //  Glide.with(holder.itemView.context).load(current.thumbnail).into(holder.productImage)
        // holder.card.setOnClickListener { com.respond(current)
        //mylistener.invoke(current)

    }



class  DiffCallback : DiffUtil.ItemCallback<HourlyWeather>(){
    override fun areItemsTheSame(oldItem: HourlyWeather, newItem: HourlyWeather): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: HourlyWeather, newItem: HourlyWeather): Boolean {
        return oldItem == newItem
    }
}