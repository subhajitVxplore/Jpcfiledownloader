package com.vxplore.jpcfiledownloader

import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri

@Composable
fun Home() {
    val context= LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val data = remember {
            mutableStateOf(
                MyFile(
                    id = "10",
                    name = "Pdf File 10 MB",
                    type = "PDF",
                    url = "https://www.learningcontainer.com/wp-content/uploads/2019/09/sample-pdf-download-10-mb.pdf",
                    downloadedUri = null
                )
            )
        }

        ItemFile(
            file = data.value,
            startDownload = {
                startDownloadingFile(
                    file = data.value,
                    success = {
                        data.value = data.value.copy().apply {
                            isDownloading = false
                            downloadedUri = it.toString()
                        }
                    },
                    failed = {
                        data.value = data.value.copy().apply {
                            isDownloading = false
                            downloadedUri = null
                        }
                    },
                    running = {
                        data.value = data.value.copy().apply {
                            isDownloading = true
                        }
                    }
                )
            },

            openFile = {
//                val intent = Intent(Intent.ACTION_VIEW)
//                intent.setDataAndType(it.downloadedUri?.toUri(),"application/pdf")
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(it.downloadedUri?.toUri(),"application/pdf")
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    startActivity(context,intent,null)
                }catch (e: ActivityNotFoundException){
                    Toast.makeText(context,"Can't open Pdf",Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}