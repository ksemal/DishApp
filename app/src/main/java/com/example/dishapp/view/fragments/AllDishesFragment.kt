package com.example.dishapp.view.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dishapp.R
import com.example.dishapp.application.DishApplication
import com.example.dishapp.databinding.FragmentAllDishesBinding
import com.example.dishapp.model.entities.Dish
import com.example.dishapp.view.activities.AddUpdateDishActivity
import com.example.dishapp.view.activities.MainActivity
import com.example.dishapp.view.adapters.DishAdapter
import com.example.dishapp.viewmodel.DishViewModel
import com.example.dishapp.viewmodel.DishViewModelFactory

class AllDishesFragment : Fragment() {

    private lateinit var mBinding: FragmentAllDishesBinding

    private val mDishViewModel: DishViewModel by activityViewModels {
        DishViewModelFactory((requireActivity().application as DishApplication).repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.rvDishesList.layoutManager = GridLayoutManager(requireActivity(), 2)

        val dishAdapter = DishAdapter(this)
        mBinding.rvDishesList.adapter = dishAdapter

        mDishViewModel.allDishesList.observe(viewLifecycleOwner) { dishes ->
            dishes.let {
                if (it.isNotEmpty()) {
                    mBinding.rvDishesList.visibility = View.VISIBLE
                    mBinding.tvNoDishesAddedYet.visibility = View.GONE
                    dishAdapter.setDishesList(it)
                } else {
                    mBinding.rvDishesList.visibility = View.GONE
                    mBinding.tvNoDishesAddedYet.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_all_dishes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_dish -> {
                startActivity(Intent(requireActivity(), AddUpdateDishActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentAllDishesBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    fun showDishDetails(dish: Dish) {
        findNavController().navigate(R.id.action_navigation_all_dishes_to_navigation_dish_details)
        (activity as? MainActivity)?.hideBottomNavigationView()
        mDishViewModel.setSelectedDish(dish)
    }

    fun deleteDish(dish: Dish) {
        AlertDialog.Builder(requireActivity())
            .setTitle(getString(R.string.title_delete_dish))
            .setMessage(getString(R.string.msg_delete_dish_dialog, dish.title))
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(getString(R.string.lbl_yes)) { dialogInterface, _ ->
                mDishViewModel.delete(dish)
                dialogInterface.dismiss()
            }
            .setNegativeButton(getString(R.string.lbl_no)) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .setCancelable(false)
            .create()
            .show()
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.showBottomNavigationView()
    }
}