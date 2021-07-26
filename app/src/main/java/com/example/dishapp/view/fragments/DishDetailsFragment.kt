package com.example.dishapp.view.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.dishapp.R
import com.example.dishapp.application.DishApplication
import com.example.dishapp.databinding.FragmentDishDetailsBinding
import com.example.dishapp.model.entities.Dish
import com.example.dishapp.viewmodel.DishViewModel
import com.example.dishapp.viewmodel.DishViewModelFactory
import java.util.*

class DishDetailsFragment : Fragment() {

    private val dishDetailsViewModel: DishViewModel by activityViewModels {
        DishViewModelFactory((requireActivity().application as DishApplication).repository)
    }
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
        dishDetailsViewModel.getSelectedDish()?.let { dish ->
            mBinding?.let { binding ->
                Glide.with(requireActivity())
                    .load(dish.image)
                    .centerCrop()
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.e("TAG", "Error loading image", e)
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            resource?.let {
                                Palette.from(it.toBitmap()).generate { palette ->
                                    val intColor = palette?.vibrantSwatch?.rgb ?: 0
                                    mBinding?.divider?.setBackgroundColor(intColor)
                                    mBinding?.tvTitle?.setTextColor(intColor)
                                }
                            }
                            return false
                        }
                    })
                    .into(binding.ivDishImage)

                binding.tvTitle.text = dish.title
                binding.tvType.text =
                    dish.type.capitalize(Locale.ROOT)
                binding.tvCategory.text = dish.category
                binding.tvIngredients.text = dish.ingredients
                binding.tvCookingDirection.text = dish.directionsToCook
                binding.tvCookingTime.text =
                    resources.getString(R.string.lbl_estimate_cooking_time, dish.cookingTime)
                binding.ivFavoriteDish.setImageResource(
                    setFavoriteDishIcon((dishDetailsViewModel.getSelectedDish() as Dish).favoriteDish)
                )
                binding.ivFavoriteDish.setOnClickListener {
                    (dishDetailsViewModel.getSelectedDish() as Dish).favoriteDish =
                        !(dishDetailsViewModel.getSelectedDish() as Dish).favoriteDish
                    dishDetailsViewModel.update(dishDetailsViewModel.getSelectedDish() as Dish)
                    binding.ivFavoriteDish.setImageResource(
                        setFavoriteDishIcon((dishDetailsViewModel.getSelectedDish() as Dish).favoriteDish)
                    )
                }
            }
        }
    }

    private fun setFavoriteDishIcon(isFavorite: Boolean): Int {
        return if (isFavorite) R.drawable.ic_favorite_selected else R.drawable.ic_favorite_unselected
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

}