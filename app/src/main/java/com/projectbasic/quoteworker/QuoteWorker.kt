package com.projectbasic.quoteworker

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.projectbasic.quoteworker.Notification.CHANNEL
import com.projectbasic.quoteworker.Worker.ERROR_KEY
import com.projectbasic.quoteworker.Worker.QUOTE_KEY
import kotlin.random.Random

class QuoteWorker(
    private val context: Context,
    private val workerParameters: WorkerParameters
):CoroutineWorker(
    context,
    workerParameters
) {
    override suspend fun doWork(): Result {

        val response=QuoteApi.retrofitInstance.fetchQuote()
        if(response.isSuccessful){
            if(response.body()!=null){
                val resp=response.body()
//                showNotification(resp?.title.toString())
                showNotificationViaManager(quote = resp?.quote.toString())
                QUOTE_KEY=resp?.quote.toString()
                return  Result.success(workDataOf(QUOTE_KEY to resp?.quote.toString() ))
            }else{
               return Result.failure(workDataOf(ERROR_KEY to "Error Response"))
            }

        }else{
            if(response.code().toString().startsWith("5")){
                return Result.retry()
            }
            return Result.failure(workDataOf(ERROR_KEY to "${response.message()+response.errorBody().toString()}"))
        }

    }


    private suspend fun showNotification(quote:String){
        val remoteView=RemoteViews(context.packageName,R.layout.notification_layout)
        remoteView.setTextViewText(R.id.notification,"$quote")
        setForeground(

            if(Build.VERSION.SDK_INT>=34){
                ForegroundInfo(
                    Random.nextInt(),
                    NotificationCompat.Builder(context,CHANNEL)
                        .setCustomContentView(remoteView)
//                        .setSmallIcon(R.drawable.ic_launcher_foreground)
//                        .setContentText("$quote")
//                        .setContentTitle("Quote")
                        .build(),

                    ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC,
                )
            }else{
                ForegroundInfo(
                    Random.nextInt(),
                    NotificationCompat.Builder(context,CHANNEL)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentText("$quote")
                        .setContentTitle("Quote")
                        .build(),
                )
            }

        )
    }

    private fun showNotificationViaManager(quote: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, CHANNEL)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentText(quote)
            .setContentTitle("Quote")
            .build()
        notificationManager.notify(Random.nextInt(), notification)
    }
}