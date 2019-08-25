package com.mageshr2494.stafftracker.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mageshr2494.stafftracker.Api.UtilsApi
import com.mageshr2494.stafftracker.Utils
import com.mageshr2494.stafftracker.model.response.login.LoginResponseEnvelope
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var utils : Utils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.mageshr2494.stafftracker.R.layout.activity_login)

        //Clear Previous data
        utils = Utils(applicationContext)
        utils.clearAllPrefs()

        loginBtn.setOnClickListener {
            if(validate())
            {
                loginProcess(userName.text.toString(), password.text.toString())
            }
        }
    }

    private fun validate(): Boolean {
        if (userName.text.isEmpty() || password.text.isEmpty()) {
            Toast.makeText(applicationContext, "Username or the password is empty", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun loginProcess(username: String, password: String) {

        val requestBodyText = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "  <soap12:Body>\n" +
                " <GetUser xmlns=\"http://salestracker.mtcweb.in/\">\n " +
                "     <BussData> \n" +
                "       <UserName>" + username + "</UserName>\n" +
                "       <Password>" + password + "</Password> \n" +
                "     </BussData>\n" +
                "    </GetUser> \n" +
                "  </soap12:Body>\n" +
                "</soap12:Envelope>"

        val requestBody = RequestBody.create("text/xml".toMediaTypeOrNull(), requestBodyText)

        val response = UtilsApi.getAPIService()?.getUserDetails(requestBody)

        response?.enqueue(object : Callback<LoginResponseEnvelope> {
            override fun onResponse(call: Call<LoginResponseEnvelope>, response: Response<LoginResponseEnvelope>) {
                val responseEnvelope = response.body()

                if (responseEnvelope != null) {
                    val userData = responseEnvelope.body?.getUserResponse?.result?.FFDTypes

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("UserID", "" + (userData?.UserID))
                    startActivity(intent)

                    //Store userid to sharedpreference
                    userData?.UserID?.let { utils.setUserId(it) }

                    Toast.makeText(applicationContext, "Login Success", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<LoginResponseEnvelope>, t: Throwable) {
                Log.e("onFailure", t.message)

            }

        })
    }
}