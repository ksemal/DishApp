package com.example.dishapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dishapp.databinding.ItemDishLayoutBinding
import com.example.dishapp.model.entities.Dish
import com.example.dishapp.view.fragments.AllDishesFragment
import com.example.dishapp.view.fragments.FavoriteDishesFragment

class DishAdapter(private val fragment: Fragment) :
    RecyclerView.Adapter<DishAdapter.ViewHolder>() {

    private var dishes: List<Dish> = emptyList()

    class ViewHolder(view: ItemDishLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val ivDishImage = view.ivDishImage
        val tvTitle = view.tvDishTitle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemDishLayoutBinding.inflate(LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dish = dishes[position]
        Glide.with(fragment)
            .load(dish.image)
            .into(holder.ivDishImage)
        holder.tvTitle.text = dish.title
        holder.itemView.setOnClickListener {
            when (fragment) {
                is AllDishesFragment -> fragment.showDishDetails(dishes[position])
                is FavoriteDishesFragment -> fragment.showDishDetails(dishes[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    fun setDishesList(list: List<Dish>) {
        dishes = list
        notifyDataSetChanged()
    }
}