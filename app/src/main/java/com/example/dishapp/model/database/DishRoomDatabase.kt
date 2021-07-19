package com.example.dishapp.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dishapp.model.entities.Dish

@Database(entities = [Dish::class], version = 1)
abstract class DishRoomDatabase: RoomDatabase() {

    companion object {

        @Volatile
        private var INSTANCE: DishRoomDatabase? = null

        fun getInstance(context: Context): DishRoomDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DishRoomDatabase::class.java,
                        "dish_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }
}