package com.example.fridgeapp1.di

import dagger.Module
import dagger.Provides

@Module
class EditFoodModule(private val selectedItemId: Int?) {
    @Provides
    fun provideItemId(): Int?{
        return selectedItemId
    }


}