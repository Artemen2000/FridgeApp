package com.example.fridgeapp1.screen.food_edit

import android.icu.text.DateFormat
import android.icu.util.Calendar
import android.text.SpannableStringBuilder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.fridgeapp1.data.FoodDao
import com.example.fridgeapp1.data.FoodItem
import com.example.fridgeapp1.util.distinctUntilChanged
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.NumberFormatException

class EditFoodViewModel(
    private val foodDao: FoodDao,
    private val selectedItemId: Int? = null
) : ViewModel() {
    private val _currentName: MutableLiveData<String> = MutableLiveData("")
    val currentName: LiveData<String> = _currentName.distinctUntilChanged()

    private val _currentDate: MutableLiveData<Long> = MutableLiveData(System.currentTimeMillis())
    val currentDate : LiveData<CurrentDate> = _currentDate.map { millis ->
        CurrentDate(
            date = DateFormat.getDateInstance().format(Date(millis)),
            days = TimeUnit.MILLISECONDS.toDays(millis - currentTimeDays()),
            millis = millis
        )
    }.distinctUntilChanged { old , new -> old.days == new.days }

    val isEditing = selectedItemId != null && selectedItemId != -1
    private val _navDest = MutableLiveData<EditFoodDestination?>()
    val navDest: LiveData<EditFoodDestination?> = _navDest
    //private lateinit var observingFood: LiveData<FoodItem>


    init {
        selectedItemId?.let { id ->
            viewModelScope.launch {
                foodDao.getItem(id).collect {
                    _currentName.value = it?.name ?: ""
                    _currentDate.value = it?.expiresAt ?: System.currentTimeMillis()
                }
            }
        }
    }

    fun currentTimeDays():Long{
        val cal = Calendar.getInstance()
        cal.set(Calendar.MILLISECONDS_IN_DAY, cal.getActualMaximum(Calendar.MILLISECONDS_IN_DAY))
        return cal.timeInMillis
    }


    fun datePicked(date: Long) {
        _currentDate.value = date
    }

    fun dateTextChanged(text: String) {
        try {
            val cal = Calendar.getInstance()
            cal.set(Calendar.MILLISECONDS_IN_DAY, cal.getActualMaximum(Calendar.MILLISECONDS_IN_DAY))
            cal.add(
                Calendar.DAY_OF_MONTH,
                if (text.isEmpty()) 0 else text.toInt()
            )
            _currentDate.value = cal.timeInMillis
        }
        catch (_: NumberFormatException){}
    }

    fun deleteItem() {
        viewModelScope.launch {
            foodDao.delete(FoodItem(
                id = selectedItemId ?: 0,
                name = currentName.value.orEmpty(),
                expiresAt = _currentDate.value ?: System.currentTimeMillis()
            ))
            navigateUp()
        }
    }

    fun addNewFood() {
        viewModelScope.launch(/*Dispatchers.IO*/) {
            foodDao.insert(
                FoodItem(
                    name = currentName.value.orEmpty(),
                    expiresAt = _currentDate.value ?: System.currentTimeMillis()
                )
            )
            navigateUp()
        }
    }

    fun updateFood() {
        viewModelScope.launch {
            foodDao.update(
                FoodItem(
                    id = selectedItemId ?: 0,
                    name = currentName.value.orEmpty(),
                    expiresAt = _currentDate.value ?: System.currentTimeMillis()
                )
            )
            navigateUp()
        }
    }

    fun acceptClicked() {
        if (isEditing) updateFood() else addNewFood()
    }

    fun navigateUp() {
        _navDest.value = EditFoodDestination.Up
    }

    fun nameChanged(name: String) {
        _currentName.value = name
    }

    override fun onCleared() {
        Log.d("goida", "VM cleared")
        super.onCleared()
        Log.d("goida", "VM cleared")
    }

}

class EditFoodViewModelFactory(
    private val foodDao: FoodDao,
    private val selectedItemId: Int?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditFoodViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditFoodViewModel(foodDao, selectedItemId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}