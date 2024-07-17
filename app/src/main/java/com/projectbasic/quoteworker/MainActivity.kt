package com.projectbasic.quoteworker

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.projectbasic.quoteworker.Worker.QUOTE_KEY
import com.projectbasic.quoteworker.ui.theme.QuoteWorkerTheme
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.time.Duration
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.coroutines.coroutineContext

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val workRequest= PeriodicWorkRequestBuilder<QuoteWorker>(repeatInterval = 15, repeatIntervalTimeUnit = TimeUnit.MINUTES).addTag(
            "mywork"
        ).build()
        val workManager=WorkManager.getInstance(applicationContext)
        workManager.pruneWork()
        workManager.cancelAllWork()



        setContent {

            QuoteWorkerTheme {
                val workInfo =
                    workManager
                        .getWorkInfosByTagLiveData("mywork")
                        .observeAsState()
                        .value
                val quoteWorkInfo =
                    remember(workInfo) {

                       workInfo?.find{
                           it.id==workRequest.id
                       }

                    }
                val quote by remember(workInfo) {
                    derivedStateOf {
                        QUOTE_KEY

                    }
                }
                val coroutineScopr= rememberCoroutineScope()
                val resp:MutableState<NewQuotes?> =remember{
                    mutableStateOf(null)
                }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                  Column(modifier = Modifier
                      .fillMaxSize()
                      .padding(innerPadding), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

                      LaunchedEffect(key1 = Unit) {
                          workManager.enqueueUniquePeriodicWork("mywork",ExistingPeriodicWorkPolicy.KEEP,workRequest)
                      }

                      Text("Quote Work: ${
                          when(quoteWorkInfo?.state){
                              WorkInfo.State.ENQUEUED -> "Enqueed"
                              WorkInfo.State.RUNNING -> "Running"
                              WorkInfo.State.SUCCEEDED -> "Success"
                              WorkInfo.State.FAILED -> "Failed"
                              WorkInfo.State.BLOCKED -> "Blocked"
                              WorkInfo.State.CANCELLED -> "Canceled"
                              null -> "Error"
                          }
                      }")
                      Button(onClick = {
                          coroutineScopr.launch {
                              resp.value=try {
                                  QuoteApi.retrofitInstance.fetchQuote().body()
                              }catch (e:Exception){
                                  Toast.makeText(this@MainActivity,e.message,Toast.LENGTH_LONG).show()
                                  null
                              }
                          }
                          }) {
                          Text("Quote:${resp.value}")
                      }

                      Text(text = quote.toString())

                  }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuoteWorkerTheme {
        Greeting("Android")
    }
}