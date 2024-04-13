package com.example.fridgeapp1.work

import android.content.Context
import android.util.Log
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class MyPeriodWorker(val cont: Context, val params: WorkerParameters): ListenableWorker(cont, params) {
    override fun startWork(): ListenableFuture<Result> {
        val res = Result.success()
        val service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10))
        val fut = service.submit(
            Callable<Result> {
                Log.d("goida", "10 seconds passed!")
                res
            }
        )
        return fut
    }
}

class MyPeriodWorker1(val cont: Context, val params: WorkerParameters): Worker(cont, params){
    override fun doWork(): Result {

        return Result.success()
    }

}
