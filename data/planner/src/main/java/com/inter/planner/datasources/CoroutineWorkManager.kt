package com.inter.planner.datasources

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class CoroutineWorkManager(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return Result.success()
    }
}