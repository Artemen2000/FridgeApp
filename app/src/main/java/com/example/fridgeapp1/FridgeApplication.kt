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
import com.example.fridgeapp1.di.AppComponent
import com.example.fridgeapp1.di.AppModule
import com.example.fridgeapp1.di.DaggerAppComponent
import com.example.fridgeapp1.work.MyWorker
import com.google.android.material.color.DynamicColors
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Duration
import javax.inject.Inject
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class FridgeApplication : Application() {
    val appComponent: AppComponent by lazy{
        DaggerAppComponent.factory().create(AppModule(this))
    }


    //val database: FoodRoomDatabase by lazy {
    //    FoodRoomDatabase.getDatabase(this)
    //}

    //@Inject
    //lateinit var database: FoodRoomDatabase

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
        //appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()

    }

    //fun getAppComponent(): AppComponent {
    //    return appComponent
    //}

}