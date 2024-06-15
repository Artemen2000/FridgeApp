package com.example.fridgeapp1.screen.food_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.fridgeapp1.data.FoodDao
import com.example.fridgeapp1.data.FoodItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Calendar

class FoodListViewModel (private val foodDao: FoodDao, var selectedItem : FoodItem? = null): ViewModel() {

    val allFood: LiveData<List<FoodItem>> = foodDao.getItems().asLiveData()

    fun isExpired(item:FoodItem, date: Date): Boolean {
        return (item.expiresAt < date.time)
    }

    fun checkExpiration(){
        val currentDate = Calendar.getInstance().time

    }

    fun updateFood(foodId: Int,
                   foodName: String,
                   expiresAt: Date){
        val updatedFood = getUpdatedFoodEntry(foodId,foodName,expiresAt)
        updateFood(updatedFood)
    }

    private fun updateFood(food: FoodItem){
        viewModelScope.launch {
            foodDao.update(food)
        }
    }

    fun deleteItem(food: FoodItem){
        viewModelScope.launch {
            foodDao.delete(food)
        }
    }

    fun retrieveItem(id: Int): LiveData<FoodItem>{
        return foodDao.getItem(id).asLiveData()
    }

    fun addNewFood(foodName: String, expiresAt: Date){
        val newFood = getNewFoodEntry(foodName, expiresAt)
        viewModelScope.launch(Dispatchers.IO) {
            foodDao.insert(newFood)
        }
    }

    private fun getNewFoodEntry(foodName: String,
                                expiresAt1: Date): FoodItem
    {
        return FoodItem(name = foodName, expiresAt = expiresAt1.time)
    }

    private fun getUpdatedFoodEntry(
        foodId: Int,
        foodName: String,
        expiresAt1: Date
    ): FoodItem{
        return FoodItem(id = foodId, name = foodName, expiresAt = expiresAt1.time)
    }
}

class FoodViewModelFactory(private val foodDao:FoodDao) : ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        if (modelClass.isAssignableFrom(FoodListViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return FoodListViewModel(foodDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}