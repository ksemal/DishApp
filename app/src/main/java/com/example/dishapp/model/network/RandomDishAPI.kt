package com.example.dishapp.model.network

import com.example.dishapp.model.entities.RandomDish
import com.example.dishapp.utils.*
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomDishAPI {

    @GET(API_ENDPOINT)
    fun getRandomDish(
        @Query(API_KEY) apiKey: String,
        @Query(LIMIT_LICENSE) limitLicense: Boolean,
        @Query(TAGS) tags: String,
        @Query(NUMBER) number: Int,
    ): Single<RandomDish.Recipe>
}