package com.example.dishapp.application

import android.app.Application
import com.example.dishapp.model.database.DishRepository
import com.example.dishapp.model.database.DishRoomDatabase

class DishApplication : Application() {
    private val dataBase by lazy {
        DishRoomDatabase.getInstance(this@DishApplication)
    }
    val repository by lazy { DishRepository(dataBase.dishDao()) }
}