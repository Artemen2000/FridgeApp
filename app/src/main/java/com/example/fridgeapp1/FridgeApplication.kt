package com.example.fridgeapp1

import android.app.Application
import android.util.Log
import android.view.View
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import com.example.fridgeapp1.data.FoodRoomDatabase
import com.example.fridgeapp1.work.MyWorker
import com.google.android.material.color.DynamicColors
import java.time.Duration
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class FridgeApplication : Application() {

    val database: FoodRoomDatabase by lazy {
        FoodRoomDatabase.getDatabase(this)
    }

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}