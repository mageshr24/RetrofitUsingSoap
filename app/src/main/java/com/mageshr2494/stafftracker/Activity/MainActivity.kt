package com.mageshr2494.stafftracker.Activity

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Address
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.Messenger
import androidx.core.app.ActivityCompat
import android.util.Log

import com.mageshr2494.stafftracker.Activity.MainActivity.messageKey.MESSENGER_INTENT_KEY
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.location.Geocoder
import com.mageshr2494.stafftracker.LocationUpdatesService
import com.mageshr2494.stafftracker.R
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

class MainActivity : AppCompatActivity() {

    var geocoder: Geocoder? = null
    var addresses: List<Address>? = null

    object messageKey {
        val MESSENGER_INTENT_KEY: String = "msg-intent-key"
    }

    val REQUEST_PERMISSIONS_REQUEST_CODE = 100
    private var mHandler: IncomingMessageHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Firebase-crashlytics
        Fabric.with(this, Crashlytics())

        overridePendingTransition(
            R.anim.slide_enter_left,
            R.anim.slide_exit_left
        )

        val id = intent.getStringExtra("ID")

        Log.v("id", "" + id)

        mHandler = IncomingMessageHandler()

        if (!checkPermissions()) {
            requestPermission()
        } else {
            mainFunction()
        }

        cardMyTarget.setOnClickListener {


//            Crashlytics.getInstance().crash(); // Force a crash

            var intent = Intent(this, MyTargetCustomerActivity::class.java)
            startActivity(intent)
        }
        cardAppointment.setOnClickListener {
            var intent = Intent(this, MyAppointmentActivity::class.java)
            startActivity(intent)
        }
        cardSalesPlanner.setOnClickListener {
            var intent = Intent(this, SalesCallPlannerActivity::class.java)
            startActivity(intent)
        }
        cardSalesCall.setOnClickListener {
            var intent = Intent(this, SalesCallActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkPermissions() =
        ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED

    private fun mainFunction() {
        val startServiceIntent = Intent(this, LocationUpdatesService::class.java)
        val messengerIncoming = Messenger(mHandler)
        startServiceIntent.putExtra(MESSENGER_INTENT_KEY, messengerIncoming)
        startService(startServiceIntent)
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Log.v("location", "locationif2")

        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSIONS_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.v("location", "locationreqif")
                    mainFunction()
                } else {
                    Log.v("location", "locationreqelse")
                }
                return
            }

            else -> {
                Log.v("location", "locationreqigno")
            }
        }
    }

    internal inner class IncomingMessageHandler : Handler() {
        override fun handleMessage(msg: Message) {
            Log.i("handleMessage", "handleMessage...$msg")
            super.handleMessage(msg)

            when (msg.what) {
                LocationUpdatesService.LOCATION_MESSAGE -> {
                    val obj = msg.obj as Location

                    geocoder = Geocoder(applicationContext, Locale.getDefault())

                    addresses = geocoder!!.getFromLocation(
                        obj.latitude,
                        obj.longitude,
                        1
                    )
                    Log.i("locationDetails", "" + addresses)

//                    val address = addresses!!.get(0).getAddressLine(0)

                    val thoroughfare = addresses!!.get(0).thoroughfare
                    val city = addresses!!.get(0).locality

                    streetName.setText("" + thoroughfare)
                    cityName.setText("" + city)
                }
            }
        }
    }
}


