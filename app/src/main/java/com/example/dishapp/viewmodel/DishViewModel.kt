package com.example.dishapp.viewmodel

import androidx.lifecycle.*
import com.example.dishapp.model.database.DishRepository
import com.example.dishapp.model.entities.Dish
import kotlinx.coroutines.launch

class DishViewModel(private val repository: DishRepository) : ViewModel() {

    fun insert(dish: Dish) = viewModelScope.launch {
        repository.insertDishData(dish)
    }

    val allDishesList: LiveData<List<Dish>> = repository.allDishesList.asLiveData()

    fun update(dish: Dish)  = viewModelScope.launch {
        repository.updateDishData(dish)
    }

    private val selectedDish: MutableLiveData<Dish> = MutableLiveData<Dish>()

    fun setSelectedDish(dish: Dish) {
        selectedDish.value = dish
    }

    fun getSelectedDish(): Dish? {
        return selectedDish.value
    }
}

class DishViewModelFactory(private val repository: DishRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DishViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DishViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}