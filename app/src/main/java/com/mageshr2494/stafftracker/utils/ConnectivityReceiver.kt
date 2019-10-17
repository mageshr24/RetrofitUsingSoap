package com.mageshr2494.stafftracker.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.mageshr2494.stafftracker.LocationUpdatesService

class ConnectivityReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("ConnectivityReceiver", "Service Stops! Oooooooooooooppppssssss!!!!")

        try {
            context?.startService(Intent(context, LocationUpdatesService::class.java))
        } catch (e: Exception) {

        }
    }

}