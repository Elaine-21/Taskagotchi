package com.mobdeve.s13.martin.elaine.taskagotchi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.martin.elaine.taskagotchi.HomeActivity
import com.mobdeve.s13.martin.elaine.taskagotchi.databinding.ItemHomeBinding
import com.mobdeve.s13.martin.elaine.taskagotchi.model.TaskagotchiData

class HomeAdapter(private val homeItem: ArrayList<TaskagotchiData>, private var activity: HomeActivity): RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.HomeViewHolder {
        val itemBinding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(itemBinding)

    }

    override fun onBindViewHolder(holder: HomeAdapter.HomeViewHolder, position: Int) {
        holder.bindHomeItem(this.homeItem[position])
//        holder.bindHomeItem(this.homeItem[position], position)
    }

    override fun getItemCount(): Int {
        return this.homeItem.size
    }

    class HomeViewHolder(private val viewBinding: ItemHomeBinding): RecyclerView.ViewHolder(viewBinding.root){
        fun bindHomeItem(model: TaskagotchiData){
            this.viewBinding.taskagotchiName.text = model.name
            //this.viewBinding.taskagotchiPicture.setImageResource(model.picURL.toInt())
            this.viewBinding.taskagotchiEnergyData.text = model.energy.toString()
            this.viewBinding.taskagotchiStreakData.text = model.streak.toString()
            this.viewBinding.taskagotchiStatusData.text = model.status.toString()
        }
    }

//    inner class HomeViewHolder(private val itemBinding: ItemHomeBinding) : RecyclerView.ViewHolder(itemBinding.root){
//        private var myPosition: Int = -1
//        private lateinit var item: HomeItem
//
//        fun bindHomeItem(homeItem: HomeItem, position: Int){
//            this@HomeViewHolder.myPosition = position
//            this@HomeViewHolder.item = homeItem
//
//            with(itemBinding){
//                taskagotchiName.text = homeItem.name
//                taskagotchiPicture.setImageURI(homeItem.picURL)
//                taskagotchiEnergyData.text = homeItem.energy
//                taskagotchiStreakData.text = homeItem.streak
//                taskagotchiStatusData.text = homeItem.status
//
//            }
//        }
//    }
}