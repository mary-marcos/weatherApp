package com.example.weatherforecast.ui.favorate

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.R
import com.example.weatherforecast.data.Repo.LocalSource
import com.example.weatherforecast.data.Repo.Repos
import com.example.weatherforecast.data.Repo.WeatherRemoteSourceImp
import com.example.weatherforecast.databinding.FragmentFavBinding
import com.example.weatherforecast.databinding.FragmentHomeBinding
import com.example.weatherforecast.databinding.FragmentMyMapBinding
import com.example.weatherforecast.model.FavItem
import com.example.weatherforecast.model.StateManager
import com.example.weatherforecast.ui.favorate.FavFragmentDirections
import com.example.weatherforecast.ui.home.DailyAdapter
import com.example.weatherforecast.ui.home.HomeViewModel
import com.example.weatherforecast.ui.home.HourlyAdapter
import com.example.weatherforecast.ui.map.FavFactory
import com.example.weatherforecast.ui.map.MapViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch


class FavFragment : Fragment() ,onFavClicked{
   lateinit var viewModel: FavViewModel
  //  private var latLng: LatLng? = null

   lateinit var favAdapter: FavLocAdapter
    lateinit var binding: FragmentFavBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var factory: FavLocationFctory =
            FavLocationFctory(Repos.getInstance(WeatherRemoteSourceImp.getInstance(), LocalSource.getInstance(requireContext())))

        viewModel = ViewModelProvider(this,factory)[FavViewModel::class.java]

        binding= FragmentFavBinding.inflate(inflater,container,false)

        val view =binding.root
        return view //inflater.inflate(R.layout.fragment_fav, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favAdapter=FavLocAdapter(this)
         binding.favRecyclerView .apply {
             adapter=favAdapter
             layoutManager = LinearLayoutManager(context)
         }

        binding.fab.setOnClickListener {
            val action = FavFragmentDirections.actionFavFragmentToMyMapFragment()
                .also { it.isfav="fav" }
            findNavController().navigate(action)
        }

        observeFavData()
        viewModel.getLocations()

    }
    private fun observeFavData() {
        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.favLocation.collect { state: StateManager<List<FavItem>?> ->
                when (state) {
                    is StateManager.Loading -> {
                         binding.homeProgressBar.visibility=View.VISIBLE
                    }
                    is StateManager.Success -> {
                        binding.homeProgressBar.visibility= View.GONE
                        favAdapter.submitList(state.data)
                    }
                    is StateManager.Error -> {
                        binding.homeProgressBar.visibility= View.GONE
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }


                    else -> {}
                }
            }
        }
    }
    private fun observedeleteData() {
        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.deletefavLocation.collect { state: StateManager<Int?> ->
                when (state) {
                    is StateManager.Loading -> {
                        binding.homeProgressBar.visibility=View.VISIBLE
                    }
                    is StateManager.Success -> {
                        binding.homeProgressBar.visibility= View.GONE
                        Toast.makeText(requireContext(), "Item {${state.data}}deleted success", Toast.LENGTH_SHORT).show()

                    }
                    is StateManager.Error -> {
                        binding.homeProgressBar.visibility= View.GONE
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }


                    else -> {}
                }
            }
        }
    }

    override fun respond(favItem: FavItem) {
        val action = FavFragmentDirections.actionFavFragmentToHomeFragment()
            .also { it.fromFav="fav,${favItem.lat},${favItem.lang}" }
       findNavController().navigate(action)
     //  Toast.makeText(requireContext(),"nav",Toast.LENGTH_SHORT).show()
    }

    override fun deletitem(favItem: FavItem) {
        AlertDialog.Builder(context)
            .setTitle("Delete Item")
            .setMessage("Are you sure you want to delete this item?")
            .setPositiveButton("Yes") { dialog, which ->
                // User clicked Yes button
                observedeleteData()
                viewModel.deleteLocationfromFav(favItem)

               // Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No") { dialog, which ->

                dialog.dismiss()
            }
            .show()
    }
}