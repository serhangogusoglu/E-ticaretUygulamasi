package com.example.e_ticaretuygulamasi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_ticaretuygulamasi.data.Food
import com.example.e_ticaretuygulamasi.databinding.ItemFoodBinding

class FoodAdapter(
    val onAdd: (Food) -> Unit
) : ListAdapter<Food, FoodAdapter.FoodViewHolder>(DiffCallback()) {

    inner class FoodViewHolder(val binding: ItemFoodBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = getItem(position)
        holder.binding.apply {
            textViewName.text = food.yemekAdi
            textViewPrice.text = "${food.yemekFiyat}₺"
            Glide.with(root).load("http://kasimadalan.pe.hu/yemekler/resimler/${food.yemekResimAdi}")
                .into(imageViewFood)
            imageViewAddToCart.setOnClickListener { onAdd(food) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Food>() {
        override fun areItemsTheSame(oldItem: Food, newItem: Food) = oldItem.yemekAdi == newItem.yemekAdi
        override fun areContentsTheSame(oldItem: Food, newItem: Food) = oldItem == newItem
    }
}
