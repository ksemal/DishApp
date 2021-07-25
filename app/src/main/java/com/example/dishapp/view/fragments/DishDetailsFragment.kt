package com.example.dishapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.dishapp.R
import com.example.dishapp.databinding.FragmentDishDetailsBinding
import com.example.dishapp.viewmodel.DishViewModel
import java.util.*

class DishDetailsFragment : Fragment() {

    private val viewModel: DishViewModel by activityViewModels()
    private var mBinding: FragmentDishDetailsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentDishDetailsBinding.inflate(inflater, container, false)
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSelectedDish()?.let { dish ->
            mBinding?.let { binding ->
                Glide.with(requireActivity())
                    .load(dish.image)
                    .centerCrop()
                    .into(binding.ivDishImage)

                binding.tvTitle.text = dish.title
                binding.tvType.text =
                    dish.type.capitalize(Locale.ROOT)
                binding.tvCategory.text = dish.category
                binding.tvIngredients.text = dish.ingredients
                binding.tvCookingDirection.text = dish.directionsToCook
                binding.tvCookingTime.text =
                    resources.getString(R.string.lbl_estimate_cooking_time, dish.cookingTime)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

}