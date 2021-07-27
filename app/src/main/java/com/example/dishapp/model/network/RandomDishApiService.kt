package com.example.dishapp.model.network

import com.example.dishapp.BuildConfig
import com.example.dishapp.model.entities.RandomDish
import com.example.dishapp.utils.BASE_URL
import com.example.dishapp.utils.LIMIT_LICENSE_VALUE
import com.example.dishapp.utils.NUMBER_VALUE
import com.example.dishapp.utils.TAGS_VALUE
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RandomDishApiService {
    private val api = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(RandomDishAPI::class.java)

    fun getRandomDish() : Single<RandomDish.Recipes> {
        return api.getRandomDish(
            BuildConfig.ProjectAPIKey,
            LIMIT_LICENSE_VALUE,
            TAGS_VALUE,
            NUMBER_VALUE)
    }
}