package com.example.testapplication

import android.app.Application
import com.example.testapplication.components.DaggerComponentTest

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DaggerComponentTest.builder().mealAPIURL(getString(R.string.meal_api))
    }
}