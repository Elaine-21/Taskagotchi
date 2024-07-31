package com.mobdeve.s13.martin.elaine.taskagotchi.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.martin.elaine.taskagotchi.HomeActivity
import com.mobdeve.s13.martin.elaine.taskagotchi.databinding.ItemHomeBinding
import com.mobdeve.s13.martin.elaine.taskagotchi.model.HomeData

class HomeAdapter(private val data: ArrayList<HomeData>/*, private var activity: HomeActivity*/): RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    private lateinit var  mListener: onItemClickListener
    interface  onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.HomeViewHolder {
        //perform viewBinding in the recyclerView
        val itemViewBinding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(itemViewBinding, mListener)
    }

    override fun onBindViewHolder(holder: HomeAdapter.HomeViewHolder, position: Int) {
        //pass the data into the viewholder with a single bind
        holder.bindData(this.data[position])
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    //viewholder binding the models data into the elements
    class HomeViewHolder(private val viewBinding: ItemHomeBinding, private val listener: onItemClickListener): RecyclerView.ViewHolder(viewBinding.root){//maybe move this?
        fun bindData(model: HomeData){
            this.viewBinding.taskagotchiName.text = model.name ?: "No Name"
            //this.viewBinding.taskagotchiPicture.setImageResource(model.picURL.toInt())
            this.viewBinding.taskagotchiEnergyData.text = model.energy?.toString() ?: "N/A"
            this.viewBinding.taskagotchiStreakData.text = model.streak?.toString() ?: "N/A"
            this.viewBinding.taskagotchiStatusData.text = model.status?.toString() ?: "N/A"
        }
        init{
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

}