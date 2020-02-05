package com.example.testapplication.components

import com.example.testapplication.MainActivity
import com.example.testapplication.MyApplication
import com.example.testapplication.providers.RetrofitProviderModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Named

@Component(modules = [RetrofitProviderModule::class])
interface ComponentTest {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun mealAPIURL(@Named("meal_api")url:String): Builder

        fun build(): ComponentTest
    }

    fun inject(mainActivity: MainActivity)

    fun inject(application: MyApplication)
}