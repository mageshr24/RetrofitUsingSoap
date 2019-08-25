package com.mageshr2494.stafftracker

import android.content.Context
import com.mageshr2494.stafftracker.Constant.TRACK_PREFERENCE
import com.mageshr2494.stafftracker.Constant.USER_ID

class Utils(val context: Context) {

    val sharedPreferences = context.getSharedPreferences(TRACK_PREFERENCE, Context.MODE_PRIVATE)

    //UserID
    fun setUserId(Count: Int) {
        val editer = sharedPreferences.edit()
        editer.putInt(USER_ID, Count)
        editer.apply()
    }

    fun getUserId(): Int {
        return sharedPreferences.getInt(USER_ID, 0)
    }

    fun clearAllPrefs() {
        val prefs = context.getSharedPreferences(Constant.TRACK_PREFERENCE, Context.MODE_PRIVATE)
        prefs.edit().clear().commit()
    }
}