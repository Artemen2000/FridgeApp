package com.example.fridgeapp1.screen.food_list

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.fridgeapp1.FridgeApplication
import com.example.fridgeapp1.data.FoodItem
import javax.inject.Inject

class FoodListViewModel @Inject constructor(application: Application): ViewModel() {

    @Inject
    lateinit var allFood: LiveData<List<FoodItem>>

    init{
        (application as FridgeApplication).appComponent.inject(this)
    }
    //fun isExpired(item:FoodItem, date: Date): Boolean {
    //    return (item.expiresAt < date.time)
    //}

    //fun checkExpiration(){
    //    val currentDate = Calendar.getInstance().time
    //}

    //fun updateFood(foodId: Int,
    //               foodName: String,
    //               expiresAt: Date){
    //    val updatedFood = getUpdatedFoodEntry(foodId,foodName,expiresAt)
    //    updateFood(updatedFood)
    //}

    //private fun updateFood(food: FoodItem){
    //    viewModelScope.launch {
    //        foodDao.update(food)
    //    }
    //}

    //fun deleteItem(food: FoodItem){
    //    viewModelScope.launch {
    //        foodDao.delete(food)
    //    }
    //}

    //fun retrieveItem(id: Int): LiveData<FoodItem>{
    //    return foodDao.getItem(id).asLiveData()
    //}

    //fun addNewFood(foodName: String, expiresAt: Date){
    //    val newFood = getNewFoodEntry(foodName, expiresAt)
    //    viewModelScope.launch(Dispatchers.IO) {
    //        foodDao.insert(newFood)
    //    }
    //}

    //private fun getNewFoodEntry(foodName: String,
    //                            expiresAt1: Date): FoodItem
    //{
    //    return FoodItem(name = foodName, expiresAt = expiresAt1.time)
    //}

    //private fun getUpdatedFoodEntry(
    //    foodId: Int,
    //    foodName: String,
    //    expiresAt1: Date
    //): FoodItem{
    //    return FoodItem(id = foodId, name = foodName, expiresAt = expiresAt1.time)
    //}
}