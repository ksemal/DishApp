package com.example.dishapp.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dishes_table")
data class Dish(
    @ColumnInfo val image: String,
    @ColumnInfo(name = "image_sourse") val imageSource: String,
    @ColumnInfo val title: String,
    @ColumnInfo val type: String,
    @ColumnInfo val category: String,
    @ColumnInfo val ingredients: String,
    @ColumnInfo(name = "cooking_time") val cookingTime: String,
    @ColumnInfo(name = "instructions") val directionsToCook: String,
    @ColumnInfo(name = "favorite_dish") val favoriteDish: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)