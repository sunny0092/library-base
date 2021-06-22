//package net.ihaha.sunny.base.settings.services
//
//import android.annotation.SuppressLint
//import android.content.Context
//import androidx.work.*
//import net.ihaha.sunny.base.presentation.listener.OnCallBackCountDownTimer
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.FlowPreview
//import kotlinx.coroutines.coroutineScope
//import org.koin.core.component.KoinComponent
//import timber.log.Timber
//import java.util.concurrent.TimeUnit
//
//
///**
// * Date: 15/03/2021.
// * @author SANG.
// * @version 1.0.0.
// */
//@ExperimentalCoroutinesApi
//@FlowPreview
//class TrackingService(
//    private val context: Context,
//    private val workerParams: WorkerParameters
//) : CoroutineWorker(context, workerParams), KoinComponent {
//
//    companion object {
//        @SuppressLint("StaticFieldLeak")
//        private const val POST_LOCATION_WORK_NAME = "location"
//        private const val TAG = "TRACKING-SERVICE: "
//        val ON_CALL_BACK_COUNT_DOWN_TIMER : OnCallBackCountDownTimer? = null
//        fun running(context: Context) {
//            val workManager = WorkManager.getInstance(context)
//            val workRequest = OneTimeWorkRequestBuilder<TrackingService>()
//                .setInitialDelay(10, TimeUnit.MILLISECONDS)
//                .build()
//            workManager.enqueueUniqueWork(
//                POST_LOCATION_WORK_NAME,
//                ExistingWorkPolicy.REPLACE,
//                workRequest
//            )
//        }
//
//        fun cancel(context: Context) {
//            WorkManager.getInstance(context).cancelUniqueWork(POST_LOCATION_WORK_NAME)
//        }
//    }
//
//    override suspend fun doWork(): Result = coroutineScope {
//        try {
//            Timber.tag(TAG).d("post Data to Server")
//            Result.success()
//        } catch (e: Exception) {
//            Result.failure()
//        }
//    }
//
//}
