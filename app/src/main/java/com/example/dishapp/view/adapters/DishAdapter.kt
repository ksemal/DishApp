package com.example.dishapp.view.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dishapp.R
import com.example.dishapp.databinding.ItemDishLayoutBinding
import com.example.dishapp.model.entities.Dish
import com.example.dishapp.utils.EXTRA_DISH_DETAILS
import com.example.dishapp.view.activities.AddUpdateDishActivity
import com.example.dishapp.view.fragments.AllDishesFragment
import com.example.dishapp.view.fragments.FavoriteDishesFragment

class DishAdapter(private val fragment: Fragment) :
    RecyclerView.Adapter<DishAdapter.ViewHolder>() {

    private var dishes: List<Dish> = emptyList()

    class ViewHolder(view: ItemDishLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val ivDishImage = view.ivDishImage
        val tvTitle = view.tvDishTitle
        val ibMore = view.ibMore
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
        holder.ibMore.setOnClickListener {
            val popup = PopupMenu(fragment.context, holder.ibMore)
            popup.menuInflater.inflate(R.menu.menu_adapter, popup.menu)
            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_delete_dish -> {
                        if (fragment is AllDishesFragment) {
                            fragment.deleteDish(dish)
                        }
                    }
                    R.id.action_edit_dish -> {
                        val intent = Intent(fragment.requireActivity(), AddUpdateDishActivity::class.java)
                        intent.putExtra(EXTRA_DISH_DETAILS, dish)
                        fragment.activity?.startActivity(intent)
                    }
                }
                true
            }
            popup.show()
        }

        holder.ibMore.visibility = when(fragment) {
            is AllDishesFragment -> View.VISIBLE
            else -> View.GONE
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