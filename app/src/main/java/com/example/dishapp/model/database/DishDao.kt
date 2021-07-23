package com.example.dishapp.model.database

import androidx.room.*
import com.example.dishapp.model.entities.Dish
import kotlinx.coroutines.flow.Flow

@Dao
interface DishDao {
    @Insert
    suspend fun insertDishDetails(dish: Dish)

    @Query("SELECT * FROM DISHES_TABLE ORDER BY ID")
    fun getAllDishesList(): Flow<List<Dish>>
}