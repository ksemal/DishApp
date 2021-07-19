package com.example.dishapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dishapp.model.database.DishRepository
import com.example.dishapp.model.entities.Dish
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class DishViewModel(private val repository: DishRepository): ViewModel() {

    fun insert(dish: Dish) = viewModelScope.launch {
        repository.insertDishData(dish)
    }
}

class DishViewModelFactory(private val repository: DishRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       if (modelClass.isAssignableFrom(DishViewModel::class.java)) {
           @Suppress("UNCHECKED_CAST")
           return DishViewModel(repository) as T
       }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}