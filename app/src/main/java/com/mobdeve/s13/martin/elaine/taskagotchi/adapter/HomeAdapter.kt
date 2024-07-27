package com.mobdeve.s13.martin.elaine.taskagotchi.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.martin.elaine.taskagotchi.HomeActivity
import com.mobdeve.s13.martin.elaine.taskagotchi.databinding.ItemHomeBinding
import com.mobdeve.s13.martin.elaine.taskagotchi.model.HomeData

//HomeData here should be an object
class HomeAdapter(private val data: ArrayList<HomeData>/*, private var activity: HomeActivity*/): RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.HomeViewHolder {
        val itemViewBinding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(itemViewBinding)

    }

    override fun onBindViewHolder(holder: HomeAdapter.HomeViewHolder, position: Int) {
        holder.bindData(this.data[position])
//        holder.bindData(this.data[position])
//        val currentItem = this.data[position]
//        Log.d("HomeAdapter", "Binding item at position $position: $currentItem")
//        holder.bindHomeItem(currentItem)
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    class HomeViewHolder(private val viewBinding: ItemHomeBinding): RecyclerView.ViewHolder(viewBinding.root){//maybe move this?
        fun bindData(model: HomeData){
//            Log.d("HomeViewHolder", "Binding HomeData: Name=${model.name}, Energy=${model.energy}, Streak=${model.streak}, Status=${model.status}")
            this.viewBinding.taskagotchiName.text = model.name ?: "No Name"
            //this.viewBinding.taskagotchiPicture.setImageResource(model.picURL.toInt())
            this.viewBinding.taskagotchiEnergyData.text = model.energy?.toString() ?: "N/A"
            this.viewBinding.taskagotchiStreakData.text = model.streak?.toString() ?: "N/A"
            this.viewBinding.taskagotchiStatusData.text = model.status?.toString() ?: "N/A"


            Log.d("HomeViewHolder", "Name visibility: ${this.viewBinding.taskagotchiName.visibility}")
            Log.d("HomeViewHolder", "Energy visibility: ${this.viewBinding.taskagotchiEnergyData.visibility}")
            Log.d("HomeViewHolder", "Streak visibility: ${this.viewBinding.taskagotchiStreakData.visibility}")
            Log.d("HomeViewHolder", "Status visibility: ${this.viewBinding.taskagotchiStatusData.visibility}")
        }
    }

}