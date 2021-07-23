package com.example.dishapp.model.database

import androidx.annotation.WorkerThread
import com.example.dishapp.model.entities.Dish
import kotlinx.coroutines.flow.Flow

class DishRepository(private val dishDao: DishDao) {

    @WorkerThread
    suspend fun insertDishData(dish: Dish) {
        dishDao.insertDishDetails(dish)
    }

    val allDishesList: Flow<List<Dish>> = dishDao.getAllDishesList()
}