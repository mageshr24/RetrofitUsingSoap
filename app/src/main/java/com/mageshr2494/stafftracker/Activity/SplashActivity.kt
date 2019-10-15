package com.mageshr2494.stafftracker.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mageshr2494.stafftracker.R
import com.mageshr2494.stafftracker.utils.SharedPreference

class SplashActivity : AppCompatActivity() {

    lateinit var utils: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        utils = SharedPreference(applicationContext)

        Handler().postDelayed({

            //Temporary
//            utils.setUserId(92)

            var userId = utils.getUserId()

            Log.v("UserId", "" + utils.getUserId())

            if (userId == 0) {
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
            }
            finish()

        }, 3000)

    }
}