package com.example.fridgeapp1

import android.content.Context
import android.graphics.Color
import android.icu.util.Calendar
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fridgeapp1.data.FoodItem
import com.example.fridgeapp1.data.getFormattedDate
import com.example.fridgeapp1.databinding.FragmentFoodListBinding
import com.example.fridgeapp1.databinding.ListItemFoodBinding

class FoodListAdapter(private val onItemClicked: (FoodItem) -> Unit):
    ListAdapter<FoodItem, FoodListAdapter.FoodViewHolder>(DiffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        return FoodViewHolder(
            ListItemFoodBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)

    }

    class FoodViewHolder(private var binding: ListItemFoodBinding):
            RecyclerView.ViewHolder(binding.root){
                fun bind(item: FoodItem){
                    binding.foodName.text = item.name
                    binding.foodExpiresAt.text = item.getFormattedDate()

                    val calendar = Calendar.getInstance()
                    if (item.expiresAt < calendar.timeInMillis)
                        binding.foodName.setBackgroundColor(Color.RED)
                }
            }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<FoodItem>(){
            override fun areItemsTheSame (oldItem: FoodItem, newItem: FoodItem): Boolean{
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: FoodItem, newItem : FoodItem): Boolean{
                return oldItem.name == newItem.name
            }
        }
    }
}