package com.mageshr2494.stafftracker

import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*

class LocationUpdatesComponent(var iLocationProvider: ILocationProvider?) {

    private var mLocationRequest: LocationRequest? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLocationCallback: LocationCallback? = null
    private var mLocation: Location? = null

    fun onCreate(context: Context) {
        Log.i(TAG, "created...............")
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                Log.i(TAG, "onCreate...onLocationResult...............loc " + locationResult!!.lastLocation)

                onNewLocation(locationResult.lastLocation)
            }
        }
        createLocationRequest()
        getLastLocation()
    }

    fun onStart() {
        Log.i(TAG, "onStart ")
        requestLocationUpdates()
    }

    fun onStop() {
        Log.i(TAG, "onStop....")
        removeLocationUpdates()
    }

    fun requestLocationUpdates() {
        Log.i(TAG, "Requesting location updates")
        try {
            mFusedLocationClient!!.requestLocationUpdates(
                mLocationRequest,
                mLocationCallback!!, Looper.getMainLooper()
            )
        } catch (unlikely: SecurityException) {
            Log.e(TAG, "Lost location permission. Could not request updates. $unlikely")
        }
    }

    fun removeLocationUpdates() {
        Log.i(TAG, "Removing location updates")
        try {
            mFusedLocationClient!!.removeLocationUpdates(mLocationCallback!!)

        } catch (unlikely: SecurityException) {

            Log.e(TAG, "Lost location permission. Could not remove updates. $unlikely")
        }
    }

    /**
     * get last location
     */
    private fun getLastLocation() {
        try {
            mFusedLocationClient!!.lastLocation
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        mLocation = task.result
                        Log.i(TAG, "getLastLocation " + mLocation!!)

                        onNewLocation(mLocation)
                    } else {
                        Log.w(TAG, "Failed to get location.")
                    }
                }
        } catch (unlikely: SecurityException) {
            Log.e(TAG, "Lost location permission.$unlikely")
        }
    }

    private fun onNewLocation(location: Location?) {
        Log.i(TAG, "New location: " + location!!)

        mLocation = location
        if (this.iLocationProvider != null) {
            this.iLocationProvider!!.onLocationUpdate(mLocation)
        }
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    interface ILocationProvider {
        fun onLocationUpdate(location: Location?)
    }

    companion object {
        private val TAG = "LocationupdateNew"
        private val UPDATE_INTERVAL_IN_MILLISECONDS = (1 * 60000/3).toLong()
        private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2
    }
}