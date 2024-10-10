package com.example.weatherforecast.ui.favorate

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.LocationFavItemBinding
import com.example.weatherforecast.model.FavItem


class FavLocAdapter(var oncflicked:onFavClicked ) : ListAdapter<FavItem, FavLocAdapter.ItemViewHolder>(DiffCallback())  {
    lateinit var binding: LocationFavItemBinding
    class ItemViewHolder(var binding: LocationFavItemBinding) : RecyclerView.ViewHolder(binding.root) {
//       val tittle:TextView=itemView.findViewById<TextView>(R.id.tittle_tv)
//        val description:TextView=itemView.findViewById<TextView>(R.id.descrip_tv)
//        val card:CardView=itemView.findViewById(R.id.cardView)
//        val productImage: ImageView= itemView.findViewById(R.id.prod_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
//databinding
        val inflater= LayoutInflater.from(parent.context)
        /////////////////
//view binding
        // val inflater :LayoutInflater= parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        ////////////////
//initial
        // view=LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)


        binding= DataBindingUtil.inflate(inflater,R.layout.location_fav_item,parent,false)

        // binding=ProductItemBinding.inflate(inflater,parent,false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current=getItem(position)
        holder.binding.favlocation=current
        holder.binding.deleteIcon.setOnClickListener { oncflicked.deletitem(current) }
        holder.itemView.setOnClickListener {oncflicked.respond(current) }
       // holder.binding.action=oncflicked

//        holder.binding.tittleTv.text=current.title
//        holder.binding.descripTv.text=current.title
        //    Glide.with(holder.itemView.context).load(current.thumbnail).into(holder.binding.prodImage)
        //  holder.binding.cardView.setOnClickListener { com.respond(current)}

        // mylistener.invoke(current)

        //  tittle.text=current.title
        // holder.description.text=current.descriptiocn
        //  Glide.with(holder.itemView.context).load(current.thumbnail).into(holder.productImage)
        // holder.card.setOnClickListener { com.respond(current)
        //mylistener.invoke(current)

    }
}


class  DiffCallback : DiffUtil.ItemCallback<FavItem>(){
    override fun areItemsTheSame(oldItem: FavItem, newItem: FavItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FavItem, newItem: FavItem): Boolean {
        return oldItem == newItem
    }
}


//@BindingAdapter("imgurl")
//fun setOnClickListener(view: ImageView, url:String) {
//    Glide.with(view.context).load(url).into(view)
//}
