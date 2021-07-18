package com.example.dishapp.utils

import android.content.Context
import com.example.dishapp.R

const val DATA_KEY: String = "data"
const val DISH_TYPE: String = "DishType"
const val DISH_CATEGORY: String = "DishCategory"
const val DISH_COOKING_TIME: String = "DishCookingTime"

fun dishTypes(context:Context): ArrayList<String> {
    return context.resources.getStringArray(R.array.dishTypes).toCollection(ArrayList())
}

fun dishCategories(context:Context): ArrayList<String> {
    return context.resources.getStringArray(R.array.dishCategories).toCollection(ArrayList())
}

fun dishCookingTime(context:Context): ArrayList<String> {
    return context.resources.getStringArray(R.array.dishCookingTime).toCollection(ArrayList())
}