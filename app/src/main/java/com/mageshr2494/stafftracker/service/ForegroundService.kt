package com.mageshr2494.stafftracker.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.ConnectivityManager
import android.os.*
import android.text.TextUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import com.mageshr2494.stafftracker.Activity.MainActivity
import com.mageshr2494.stafftracker.Activity.MainActivity.messageKey.MESSENGER_INTENT_KEY
import com.mageshr2494.stafftracker.Api.UtilsApi
import com.mageshr2494.stafftracker.LocationUpdatesComponent
import com.mageshr2494.stafftracker.R
import com.mageshr2494.stafftracker.db.LatLongReq
import com.mageshr2494.stafftracker.db.SqlLiteDBHelper
import com.mageshr2494.stafftracker.model.response.locationTracking.LocationTrackEnvelope
import com.mageshr2494.stafftracker.utils.SharedPreference
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForegroundService : Service(), LocationUpdatesComponent.ILocationProvider {

    lateinit var utils: SharedPreference
    private var mActivityMessenger: Messenger? = null
    var userId: Int = 0

    companion object {
        val LOCATION_MESSAGE = 9999
    }

    private var locationUpdatesComponent: LocationUpdatesComponent? = null
    var Tag: String = "ForegroundService"

    val sqlLiteDBHelper: SqlLiteDBHelper = SqlLiteDBHelper(this)

    override fun onLocationUpdate(location: Location?) {
        Log.v(Tag, "" + location)

        location?.let { sendMessage(LOCATION_MESSAGE, it) }

        if (userId != 0) {

            val datalist: List<LatLongReq> = sqlLiteDBHelper.getData()

            if (isConnectedOrConnecting(this)) {

                sendLocationDetails(location!!.latitude, location!!.longitude)

                Log.v("isconnection", "datavalue - " + datalist)

                if (datalist.size > 0) {

                    Log.v("isconnection", "datavalue1 - " + datalist)
                    sendDbData(datalist)
                }

            } else {

                sqlLiteDBHelper.addLatLong(location!!.latitude, location!!.longitude)

                if (datalist.size > 0) {
                    Log.v("isconnection", "data - " + datalist[0].latitude)
                }
            }
        }
    }

    private fun sendDbData(datalist: List<LatLongReq>) {

        for (data in datalist) {

            sqlLiteDBHelper.deleteData(data)
            sendLocationDetails(data!!.latitude, data!!.longitude)
        }
    }

    private val CHANNEL_ID = "ForegroundService Kotlin"

    override fun onCreate() {

        utils = SharedPreference(applicationContext)

        userId = utils.getUserId()
        Log.i(Tag, "created...............")


        locationUpdatesComponent = LocationUpdatesComponent(this)
        locationUpdatesComponent!!.onCreate(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //do heavy work on a background thread
        mActivityMessenger = intent?.getParcelableExtra(MESSENGER_INTENT_KEY)
        val input = intent?.getStringExtra("message")

        createNotificationChannel()

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("StaffTracker")
            .setContentText(input)
            .setSmallIcon(R.drawable.ic_callplaner)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)

        Log.i(Tag, "onStartCommand Service started")

        locationUpdatesComponent!!.onStart()

        //stopSelf();
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.i("EXIT", "ondestroy!")

        val broadcastIntent = Intent("ac.in.ActivityRecognition.RestartSensor")
        sendBroadcast(broadcastIntent)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }

    private fun sendMessage(messageID: Int, location: Location) {

        if (mActivityMessenger == null) {
            Log.d(Tag, "Service is bound, not started. There's no callback to send a message to.")
            return
        }

        if (!isConnectedOrConnecting(this)) {

            Log.d(
                Tag,
                "Connection issue. Service is bound, not started. There's no callback to send a message to."
            )
            return
        }

        val m = Message.obtain()
        m.what = messageID
        m.obj = location

        try {
            mActivityMessenger!!.send(m)
            Log.v("UserId", "" + userId)

        } catch (e: RemoteException) {
            Log.e(Tag, "Error passing service object back to activity.")
        }
    }

    fun isConnectedOrConnecting(context: Context): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
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

                    if (TextUtils.isEmpty(trackingResult?.result)) {
                        Log.v(Tag, "trackingResult")
                    }
                }
            }

            override fun onFailure(call: Call<LocationTrackEnvelope>, t: Throwable) {
                Log.e("onFailure", "" + t)
            }
        })
    }
}