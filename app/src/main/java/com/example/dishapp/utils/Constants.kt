package com.example.dishapp.utils

import android.content.Context
import com.example.dishapp.R

const val DATA_KEY: String = "data"
const val DISH_TYPE: String = "DishType"
const val DISH_CATEGORY: String = "DishCategory"
const val DISH_COOKING_TIME: String = "DishCookingTime"

const val DISH_IMAGE_SOURCE_LOCAL: String = "Local"
const val DISH_IMAGE_SOURCE_ONLINE: String = "Online"

const val EXTRA_DISH_DETAILS: String = "DishDetails"

const val ALL_ITEMS: String = "All"
const val FILTER_SELECTION: String = "FilterSelection"

const val API_ENDPOINT: String = "recipes/random"
const val API_KEY: String = "apiKey"
const val LIMIT_LICENSE: String = "limitLicense"
const val TAGS: String = "tags"
const val NUMBER: String = "number"

const val BASE_URL: String = "https://api.spoonacular.com/"
const val LIMIT_LICENSE_VALUE: Boolean = true
const val TAGS_VALUE: String = "vegetarian, dessert"
const val NUMBER_VALUE: Int = 1

fun dishTypes(context: Context): ArrayList<String> {
    return context.resources.getStringArray(R.array.dishTypes).toCollection(ArrayList())
}

fun dishCategories(context: Context): ArrayList<String> {
    return context.resources.getStringArray(R.array.dishCategories).toCollection(ArrayList())
}

fun dishCookingTime(context: Context): ArrayList<String> {
    return context.resources.getStringArray(R.array.dishCookingTime).toCollection(ArrayList())
}