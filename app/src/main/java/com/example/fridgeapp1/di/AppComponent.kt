package com.example.fridgeapp1.di

import com.example.fridgeapp1.FridgeApplication
import com.example.fridgeapp1.screen.food_list.FoodListFragment
import com.example.fridgeapp1.screen.food_list.FoodListViewModel
import dagger.Component

import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, AppSubcomponents::class])
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(appModule: AppModule): AppComponent
    }
    fun inject(foodListViewModel: FoodListViewModel)
    fun inject(foodListViewModel: FoodListFragment)
    fun inject(fridgeApplication: FridgeApplication)

    fun editFoodComponent(): EditFoodComponent.Factory

    //fun hello(){
    //    Log.d("FridgeApp", "Hello from AppComponent!")
    //}
}