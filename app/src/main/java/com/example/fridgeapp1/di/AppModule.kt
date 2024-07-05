package com.example.fridgeapp1.di

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.fridgeapp1.data.FoodDao
import com.example.fridgeapp1.data.FoodItem
import com.example.fridgeapp1.data.FoodRoomDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {
    @Singleton
    @Provides
    fun getFoodDao(database: FoodRoomDatabase): FoodDao{
        return database.getFoodDao()
    }
    @Singleton
    @Provides
    fun getRoomDbInstance(): FoodRoomDatabase{
        return FoodRoomDatabase.getDatabase(application.applicationContext)
    }

    @Provides
    fun provideApplication(): Application{
        return application
    }

    @Provides
    fun provideAllFood(): LiveData<List<FoodItem>> {
        return getFoodDao(getRoomDbInstance()).getItems().asLiveData()
    }


}