package com.example.weatherforecast.ui.home

import com.example.weatherforecast.databinding.DailyItemBinding



import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecast.databinding.ItemHourlyBinding
import com.example.weatherforecast.model.DailyWeather
import com.example.weatherforecast.model.ForecastWeatherData

class DailyAdapter():  ListAdapter<DailyWeather, DailyAdapter.ItemViewHolder>(DiffCallback1())  {
    lateinit var binding: DailyItemBinding
    class ItemViewHolder(var binding: DailyItemBinding) : RecyclerView.ViewHolder(binding.root) {
//       val tittle:TextView=itemView.findViewById<TextView>(R.id.tittle_tv)
//        val description:TextView=itemView.findViewById<TextView>(R.id.descrip_tv)
//        val card:CardView=itemView.findViewById(R.id.cardView)
//        val productImage: ImageView= itemView.findViewById(R.id.prod_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        //parent.context.getSystemService(context.LAYOUT_INF)
        //LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        binding=DailyItemBinding.inflate(inflater,parent,false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current=getItem(position)
        holder.binding.day.text=current.day
        holder.binding.date.text=current.date
        holder.binding.tempRange.text= current.minmaxTemp

        Glide.with(holder.itemView.context).load(current.iconImg).into(holder.binding.weatherIcon)
//        holder.binding.
    //    cardView.setOnClickListener { listen.listener(current)
    }
    // mylistener.invoke(current)

    //  tittle.text=current.title
    // holder.description.text=current.descriptiocn
    //  Glide.with(holder.itemView.context).load(current.thumbnail).into(holder.productImage)
    // holder.card.setOnClickListener { com.respond(current)
    //mylistener.invoke(current)

}



class  DiffCallback1 : DiffUtil.ItemCallback<DailyWeather>(){
    override fun areItemsTheSame(oldItem: DailyWeather, newItem: DailyWeather): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: DailyWeather, newItem: DailyWeather): Boolean {
        return oldItem == newItem
    }
}