package com.vxplore.jpcfiledownloader

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class FileDownloadWorker(
    private val context: Context,
    workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters) {


    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {

        val fileUrl = inputData.getString(FileParams.KEY_FILE_URL) ?: ""
        val fileName = inputData.getString(FileParams.KEY_FILE_NAME) ?: ""
        val fileType = inputData.getString(FileParams.KEY_FILE_TYPE) ?: ""

        Log.d("TAG", "doWork: $fileUrl | $fileName | $fileType")


        if (fileName.isEmpty()
            || fileType.isEmpty()
            || fileUrl.isEmpty()
        ){
            Result.failure()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val name = NotificationConstants.CHANNEL_NAME
            val description = NotificationConstants.CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(NotificationConstants.CHANNEL_ID,name,importance)
            channel.description = description

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)

        }

        val builder = NotificationCompat.Builder(context,NotificationConstants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Downloading your file...")
            .setOngoing(true)
            .setProgress(0,0,true)

        NotificationManagerCompat.from(context).notify(NotificationConstants.NOTIFICATION_ID,builder.build())

        val uri = getSavedFileUri(
            fileName = fileName,
            fileType = fileType,
            fileUrl = fileUrl,
            context = context
        )

        NotificationManagerCompat.from(context).cancel(NotificationConstants.NOTIFICATION_ID)
        return if (uri != null){
            Result.success(workDataOf(FileParams.KEY_FILE_URI to uri.toString()))
        }else{
            Result.failure()
        }

    }



    object FileParams{
        const val KEY_FILE_URL = "key_file_url"
        const val KEY_FILE_TYPE = "key_file_type"
        const val KEY_FILE_NAME = "key_file_name"
        const val KEY_FILE_URI = "key_file_uri"
    }

    object NotificationConstants{
        const val CHANNEL_NAME = "download_file_worker_demo_channel"
        const val CHANNEL_DESCRIPTION = "download_file_worker_demo_description"
        const val CHANNEL_ID = "download_file_worker_demo_channel_123456"
        const val NOTIFICATION_ID = 1
    }
}