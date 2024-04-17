package com.example.fridgeapp1.work

import android.R
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.annotations.Async
import java.util.concurrent.Callable
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class MyWorker(val cont: Context, val params: WorkerParameters) : ListenableWorker(cont, params)
{
    override fun startWork(): ListenableFuture<Result> {
        val res = Result.success()
        val service1 = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10))
        val fut2 = service1.submit (
            Callable<Result> {
                res
            }
        )
        return fut2
    }

}
