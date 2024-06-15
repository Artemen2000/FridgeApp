package com.example.fridgeapp1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.fridgeapp1.data.FoodItem
import com.example.fridgeapp1.work.MyPeriodWorker
import com.example.fridgeapp1.work.MyWorker
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.common.util.concurrent.ListenableFuture
import java.time.Duration
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setSupportActionBar(findViewById(R.id.appBar))
        setupActionBarWithNavController(this, navController)
    }

    override fun onStart() {
        super.onStart()
        //startWork()
        stopWork()
    }

    override fun onSupportNavigateUp(): Boolean{
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun startWork(){
        val workManager = WorkManager.getInstance(this)
        //val myWorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
            //.setInitialDelay(Duration.ofSeconds(5))
            //.build()



        val periodReq = PeriodicWorkRequestBuilder<MyPeriodWorker>(10, TimeUnit.SECONDS)
            .build()
        workManager.enqueue(periodReq)
    }

    fun stopWork(){
        val workManager = WorkManager.getInstance(this)
        //val myWorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
        //.setInitialDelay(Duration.ofSeconds(5))
        //.build()



        val periodReq = PeriodicWorkRequestBuilder<MyPeriodWorker>(10, TimeUnit.SECONDS)
            .build()
        //workManager.enqueue(periodReq)
        val a = workManager.getWorkInfoById(periodReq.id)
        workManager.cancelAllWork()
    }
}

