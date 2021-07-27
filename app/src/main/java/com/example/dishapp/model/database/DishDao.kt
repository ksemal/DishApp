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

    @Update
    suspend fun updateDishDetails(dish: Dish)

    @Query("SELECT * FROM DISHES_TABLE WHERE favorite_dish = 1")
    fun getAllFavoriteDishesList(): Flow<List<Dish>>

    @Delete
    suspend fun deleteDishDetails(dish: Dish)

    @Query("SELECT * FROM DISHES_TABLE WHERE type = :filteredType")
    fun getFilteredDishesList(filteredType: String): Flow<List<Dish>>
}