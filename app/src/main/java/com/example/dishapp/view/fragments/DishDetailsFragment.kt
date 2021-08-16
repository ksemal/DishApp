package com.example.dishapp.view.fragments

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.*
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
import com.example.dishapp.utils.DISH_IMAGE_SOURCE_ONLINE
import com.example.dishapp.viewmodel.DishViewModel
import com.example.dishapp.viewmodel.DishViewModelFactory
import java.util.*

class DishDetailsFragment : Fragment() {

    private var mDishDetail: Dish? = null

    private val dishDetailsViewModel: DishViewModel by activityViewModels {
        DishViewModelFactory((requireActivity().application as DishApplication).repository)
    }
    private var mBinding: FragmentDishDetailsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_share, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share_dish -> {
                val type = "text/plain"
                val subject = "Checkout this dish recipe"
                var extraText = ""
                val shareWith = "Share with"

                mDishDetail?.let {
                    var image = ""
                    if (it.imageSource == DISH_IMAGE_SOURCE_ONLINE) {
                        image = it.image
                    }

                    var cookingInstructions = ""

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        cookingInstructions = Html.fromHtml(
                            it.directionsToCook,
                            Html.FROM_HTML_MODE_COMPACT
                        ).toString()
                    } else {
                        @Suppress("DEPRECATION")
                        cookingInstructions = Html.fromHtml(it.directionsToCook).toString()
                    }

                    extraText =
                        "$image \n" +
                                "\n Title:  ${it.title} \n\n Type: ${it.type} \n\n Category: ${it.category}" +
                                "\n\n Ingredients: \n ${it.ingredients} \n\n Instructions To Cook: \n $cookingInstructions" +
                                "\n\n Time required to cook the dish approx ${it.cookingTime} minutes."
                }


                val intent = Intent(Intent.ACTION_SEND)
                intent.type = type
                intent.putExtra(Intent.EXTRA_SUBJECT, subject)
                intent.putExtra(Intent.EXTRA_TEXT, extraText)
                startActivity(Intent.createChooser(intent, shareWith))

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

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
            mDishDetail = dish
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.tvCookingDirection.text = Html.fromHtml(
                        dish.directionsToCook,
                        Html.FROM_HTML_MODE_COMPACT
                    )
                } else {
                    @Suppress("DEPRECATION")
                    binding.tvCookingDirection.text =
                        Html.fromHtml(dish.directionsToCook)
                }
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