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

class HourlyAdapter():  ListAdapter<ForecastWeatherData, HourlyAdapter.ItemViewHolder>(DiffCallback())  {
    lateinit var binding: ItemHourlyBinding
    class ItemViewHolder(var binding: ItemHourlyBinding) : RecyclerView.ViewHolder(binding.root) {
//       val tittle:TextView=itemView.findViewById<TextView>(R.id.tittle_tv)
//        val description:TextView=itemView.findViewById<TextView>(R.id.descrip_tv)
//        val card:CardView=itemView.findViewById(R.id.cardView)
//        val productImage: ImageView= itemView.findViewById(R.id.prod_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        //parent.context.getSystemService(context.LAYOUT_INF)
        //LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        binding=ItemHourlyBinding.inflate(inflater,parent,false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current=getItem(position)
        holder.binding.tvTime.text=current.dt_txt.toString()
      //  tittleTv.text=current.title
        holder.binding.tvTemperature.text=current.main.temp.toString()

        Glide.with(holder.itemView.context).load(current.weather.get(0).icon).into(holder.binding.IconView)
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



class  DiffCallback : DiffUtil.ItemCallback<ForecastWeatherData>(){
    override fun areItemsTheSame(oldItem: ForecastWeatherData, newItem: ForecastWeatherData): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: ForecastWeatherData, newItem: ForecastWeatherData): Boolean {
        return oldItem == newItem
    }
}