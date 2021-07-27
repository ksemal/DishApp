package com.example.dishapp.view.fragments

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.dishapp.R
import com.example.dishapp.application.DishApplication
import com.example.dishapp.databinding.FragmentRandomDishBinding
import com.example.dishapp.model.entities.Dish
import com.example.dishapp.model.entities.RandomDish
import com.example.dishapp.utils.DISH_IMAGE_SOURCE_ONLINE
import com.example.dishapp.viewmodel.DishViewModel
import com.example.dishapp.viewmodel.DishViewModelFactory
import com.example.dishapp.viewmodel.RandomDishViewModel

class RandomDishFragment : Fragment() {
    private var mBinding: FragmentRandomDishBinding? = null
    private var isFavorite: Boolean = false
    private val mFavoriteDishViewModel: DishViewModel by activityViewModels {
        DishViewModelFactory((requireActivity().application as DishApplication).repository)
    }
    private lateinit var mRandomDishViewModel: RandomDishViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentRandomDishBinding.inflate(inflater, container, false)
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRandomDishViewModel = ViewModelProvider(this).get(RandomDishViewModel::class.java)
        mRandomDishViewModel.getRandomRecipeFromAPI()
        randomDishViewModelObserver()
        mBinding?.srlRandomDish?.setOnRefreshListener {
            mRandomDishViewModel.getRandomRecipeFromAPI()
        }
    }

    private fun randomDishViewModelObserver() {
        mRandomDishViewModel.randomDishResponse.observe(viewLifecycleOwner, { randomDishResponse ->
            randomDishResponse?.let {
                mBinding?.let { binding ->
                    if (binding.srlRandomDish.isRefreshing) {
                        mBinding?.srlRandomDish?.isRefreshing = false
                    }
                }
                setRandomDishResponseInUi(randomDishResponse.recipes[0])
            }
        })

        mRandomDishViewModel.randomDishLoadingError.observe(viewLifecycleOwner, { dataError ->
            dataError?.let {
                Log.e("Random dish API error", "$it")
            }
            mBinding?.let { binding ->
                if (binding.srlRandomDish.isRefreshing) {
                    mBinding?.srlRandomDish?.isRefreshing = false
                }
            }
        })

        mRandomDishViewModel.loadRandomDish.observe(viewLifecycleOwner, { loadRandomDish ->
            loadRandomDish?.let {
                Log.i("Loading random dish", "$loadRandomDish")
            }
        })

    }

    private fun setRandomDishResponseInUi(recipe: RandomDish.Recipe) {
        mBinding?.let { binding ->
            Glide.with(requireActivity())
                .load(recipe.image)
                .centerCrop()
                .into(binding.ivDishImage)

            binding.tvTitle.text = recipe.title

            // Default Dish Type
            var dishType: String = getString(R.string.other)
            if (recipe.dishTypes.isNotEmpty()) {
                dishType = recipe.dishTypes[0]
                binding.tvType.text = dishType
            }

            binding.tvCategory.text = getString(R.string.other)

            var ingredients: String = ""
            for (value in recipe.extendedIngredients) {
                ingredients = if (ingredients.isEmpty()) {
                    value.original
                } else {
                    ingredients + ", \n" + value.original
                }
            }
            binding.tvIngredients.text = ingredients

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.tvCookingDirection.text = Html.fromHtml(
                    recipe.instructions,
                    Html.FROM_HTML_MODE_COMPACT
                )
            } else {
                @Suppress("DEPRECATION")
                binding.tvCookingDirection.text = Html.fromHtml(recipe.instructions)
            }

            binding.tvCookingTime.text =
                resources.getString(
                    R.string.lbl_estimate_cooking_time,
                    recipe.readyInMinutes.toString()
                )

            binding.ivFavoriteDish.setOnClickListener {
                if (isFavorite) {
                    Toast.makeText(
                        requireActivity(),
                        resources.getString(R.string.msg_already_added_to_favorites),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    isFavorite = !isFavorite
                    val randomDishDetails = Dish(
                        recipe.image,
                        DISH_IMAGE_SOURCE_ONLINE,
                        recipe.title,
                        dishType,
                        "Other",
                        ingredients,
                        recipe.readyInMinutes.toString(),
                        recipe.instructions,
                        true
                    )

                    mFavoriteDishViewModel.insert(randomDishDetails)
                    binding.ivFavoriteDish.setImageResource(
                        setFavoriteDishIcon(isFavorite)
                    )

                    Toast.makeText(
                        requireActivity(),
                        resources.getString(R.string.msg_added_to_favorites),
                        Toast.LENGTH_SHORT
                    ).show()
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