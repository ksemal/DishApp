package com.example.dishapp.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.dishapp.R
import com.example.dishapp.viewmodel.DishViewModel

class DishDetailsFragment : Fragment() {

    private val viewModel: DishViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val root = inflater.inflate(R.layout.fragment_dish_details, container, false)
        println("Selected dish ${viewModel.getSelectedDish()?.title}")
        return root
    }

}