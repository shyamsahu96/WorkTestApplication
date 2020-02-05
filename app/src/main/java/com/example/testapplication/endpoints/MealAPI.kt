package com.example.testapplication.endpoints

import com.example.testapplication.models.Post
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface MealAPI {
    @GET("posts")
    suspend fun getMeals(): Response<List<Post>>
}