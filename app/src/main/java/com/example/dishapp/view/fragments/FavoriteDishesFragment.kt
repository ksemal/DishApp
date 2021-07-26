package com.example.dishapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dishapp.application.DishApplication
import com.example.dishapp.databinding.FragmentFavoriteDishesBinding
import com.example.dishapp.view.adapters.DishAdapter
import com.example.dishapp.viewmodel.DishViewModel
import com.example.dishapp.viewmodel.DishViewModelFactory

class FavoriteDishesFragment : Fragment() {

    private val mFavoriteDishViewModel: DishViewModel by viewModels {
        DishViewModelFactory((requireActivity().application as DishApplication).repository)
    }

    private var mBinding: FragmentFavoriteDishesBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentFavoriteDishesBinding.inflate(inflater, container, false)
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding?.let { binding ->
            binding.rvFavoriteDishesList.layoutManager = GridLayoutManager(requireActivity(), 2)

            val dishAdapter = DishAdapter(this)
            binding.rvFavoriteDishesList.adapter = dishAdapter

            mFavoriteDishViewModel.favoriteDishesList.observe(viewLifecycleOwner) { dishes ->
                dishes.let {
                    if (it.isNotEmpty()) {
                        binding.rvFavoriteDishesList.visibility = View.VISIBLE
                        binding.tvNoFavoriteDishesAddedYet.visibility = View.GONE
                        dishAdapter.setDishesList(it)
                    } else {
                        binding.rvFavoriteDishesList.visibility = View.GONE
                        binding.tvNoFavoriteDishesAddedYet.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}