package com.vxplore.jpcfiledownloader

import androidx.lifecycle.LiveData
import androidx.work.*

fun startDownloadingFile(
    file: MyFile,
    success:(String) -> Unit,
    failed:(String) -> Unit,
    running:() -> Unit
    ): OneTimeWorkRequest {
        val data = Data.Builder()

        data.apply {
            putString(FileDownloadWorker.FileParams.KEY_FILE_NAME, file.name)
            putString(FileDownloadWorker.FileParams.KEY_FILE_URL, file.url)
            putString(FileDownloadWorker.FileParams.KEY_FILE_TYPE, file.type)
        }

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .setRequiresBatteryNotLow(true)
            .build()

        return OneTimeWorkRequestBuilder<FileDownloadWorker>()
            .setConstraints(constraints)
            .setInputData(data.build())
            .build()




//    val workManager = WorkManager.getInstance(this)
//    workManager.getWorkInfoByIdLiveData(fileDownloadWorker.id)
//        .observe(this){ info->
//            info?.let {
//                when (it.state) {
//                    WorkInfo.State.SUCCEEDED -> {
//                        success(it.outputData.getString(FileDownloadWorker.FileParams.KEY_FILE_URI) ?: "")
//                    }
//                    WorkInfo.State.FAILED -> {
//                        failed("Downloading failed!")
//                    }
//                    WorkInfo.State.RUNNING -> {
//                        running()
//                    }
//                    else -> {
//                        failed("Something went wrong")
//                    }
//                }
//            }
//        }
//







    }


