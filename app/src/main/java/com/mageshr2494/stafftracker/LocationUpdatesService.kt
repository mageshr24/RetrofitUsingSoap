package com.mageshr2494.stafftracker

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.net.ConnectivityManager
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.text.TextUtils.isEmpty
import android.util.Log
import com.mageshr2494.stafftracker.Api.UtilsApi
import com.mageshr2494.stafftracker.Activity.MainActivity.messageKey.MESSENGER_INTENT_KEY
import com.mageshr2494.stafftracker.model.response.locationTracking.LocationTrackEnvelope
import com.mageshr2494.stafftracker.utils.ConnectivityReceiver
import com.mageshr2494.stafftracker.utils.SharedPreference
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationUpdatesService : JobService(), LocationUpdatesComponent.ILocationProvider {

    lateinit var utils: SharedPreference
    private var mActivityMessenger: Messenger? = null
    var userId: Int = 0

    private var locationUpdatesComponent: LocationUpdatesComponent? = null

    override fun onStartJob(params: JobParameters): Boolean {
        Log.i(TAG, "onStartJob....")
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        Log.i(TAG, "onStopJob....")
        locationUpdatesComponent!!.onStop()

        return false
    }

    override fun onCreate() {

        utils = SharedPreference(applicationContext)

        userId = utils.getUserId()
        Log.i(TAG, "created...............")

        locationUpdatesComponent = LocationUpdatesComponent(this)

        locationUpdatesComponent!!.onCreate(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand Service started")

        if (intent != null) {
            mActivityMessenger = intent.getParcelableExtra(MESSENGER_INTENT_KEY)
        }

        //hey request for location updates
        locationUpdatesComponent!!.onStart()

        return Service.START_STICKY
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onRebind(intent: Intent) {
        // Called when a client (MainActivity in case of this sample) returns to the foreground
        // and binds once again with this service. The service should cease to be a foreground
        // service when that happens.
        Log.i(TAG, "in onRebind()")
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent): Boolean {
        Log.i(TAG, "Last client unbound from service")

        return true // Ensures onRebind() is called when a client re-binds.
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy....")
    }

    /**
     * send message by using messenger
     *
     * @param messageID
     */
    private fun sendMessage(messageID: Int, location: Location) {
        // If this service is launched by the JobScheduler, there's no callback Messenger. It
        // only exists when the MainActivity calls startService() with the callback in the Intent.
        if (mActivityMessenger == null) {
            Log.d(TAG, "Service is bound, not started. There's no callback to send a message to.")
            return
        }
        val m = Message.obtain()
        m.what = messageID
        m.obj = location

        try {
            mActivityMessenger!!.send(m)
            Log.v("UserId", "" + userId)

            /*if (userId != 0) {
                sendLocationDetails(location.latitude, location.longitude)
            }*/

        } catch (e: RemoteException) {
            Log.e(TAG, "Error passing service object back to activity.")
        }
    }

    private fun sendLocationDetails(latitude: Double, longitude: Double) {

        val requestBodyText = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "  <soap12:Body>\n" +
                " <DailyTrackingSave xmlns=\"http://salestracker.mtcweb.in/\">\n " +
                "     <BussData> \n" +
                "       <UserID>" + userId + "</UserID>\n" +
                "       <Latitude>" + latitude + "</Latitude> \n" +
                "       <Longitude>" + longitude + "</Longitude> \n" +
                "     </BussData>\n" +
                "    </DailyTrackingSave> \n" +
                "  </soap12:Body>\n" +
                "</soap12:Envelope>"

        val requestBody = RequestBody.create("text/xml".toMediaTypeOrNull(), requestBodyText)

        val response = UtilsApi.getAPIService()?.locationTrack(requestBody)

        response?.enqueue(object : Callback<LocationTrackEnvelope> {
            override fun onResponse(
                call: Call<LocationTrackEnvelope>,
                response: Response<LocationTrackEnvelope>
            ) {
                val responseEnvelope = response.body()

                if (responseEnvelope != null) {
                    val trackingResult = responseEnvelope!!.body?.dailyTrackingSaveResponse

                    if (isEmpty(trackingResult?.result)) {
                        Log.v(TAG, "trackingResult")
                    }
                }
            }

            override fun onFailure(call: Call<LocationTrackEnvelope>, t: Throwable) {
                Log.e("onFailure", t.message)
            }
        })
    }

    override fun onLocationUpdate(location: Location?) {
        location?.let { sendMessage(LOCATION_MESSAGE, it) }

        if (userId != 0) {

            if (isConnectedOrConnecting(this)) {

                Log.v("isconnection", "true")
                sendLocationDetails(location!!.latitude, location!!.longitude)
            } else {
                Log.v("isconnection", "false")
            }
        }
    }

    fun isConnectedOrConnecting(context: Context): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }

    companion object {
        private val TAG = "BackgroundService"
        val LOCATION_MESSAGE = 9999
    }
}