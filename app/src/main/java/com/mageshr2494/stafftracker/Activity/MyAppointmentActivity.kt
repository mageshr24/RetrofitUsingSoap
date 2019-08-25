package com.mageshr2494.stafftracker.Activity

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.mageshr2494.stafftracker.Api.UtilsApi
import com.mageshr2494.stafftracker.R
import com.mageshr2494.stafftracker.Utils
import com.mageshr2494.stafftracker.model.response.locationTracking.MyAppointmentEnvelope
import kotlinx.android.synthetic.main.activity_my_appointment.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MyAppointmentActivity : AppCompatActivity() {
    lateinit var utils: Utils
    var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_appointment)

        utils = Utils(applicationContext)
        userId = utils.getUserId()

        //actionbar
        val actionbar = supportActionBar
        actionbar!!.title = "My Appointment"
        actionbar.setBackgroundDrawable(ColorDrawable(Color.parseColor("#00000000")))
        actionbar.setDisplayHomeAsUpEnabled(true)

        appointmentDate.setOnClickListener {
            val c = Calendar.getInstance()
            val mYear = c.get(Calendar.YEAR)
            val mMonth = c.get(Calendar.MONTH)
            val mDay = c.get(Calendar.DAY_OF_MONTH)
            // date picker dialog
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    val myFormat = "yyyy-MM-dd" // mention the format you need
                    val sdf = SimpleDateFormat(myFormat, Locale.US)
                    appointmentDate.setText(sdf.format(c.getTime()))

                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }

        salesCallBtn.setOnClickListener {

            if (validate()) {

                sendMyAppointment(
                    appointmentDate.text.toString(),
                    customerName.text.toString(),
                    specialNote.text.toString()
                )

            }
        }
    }

    private fun sendMyAppointment(appointmentDate: String, customerName: String, specialNote: String) {
        val requestBodyText = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "  <soap12:Body>\n" +
                " <CustomersAppointment xmlns=\"http://salestracker.mtcweb.in/\">\n " +
                "     <BussData> \n" +
                "       <UserID>" + userId + "</UserID>\n" +
                "       <AppointmentDt>" + appointmentDate + "</AppointmentDt> \n" +
                "       <CustName >" + customerName + "</CustName > \n" +
                "       <Remarks>" + specialNote + "</Remarks> \n" +
                "     </BussData>\n" +
                "    </CustomersAppointment> \n" +
                "  </soap12:Body>\n" +
                "</soap12:Envelope>"

        val requestBody = RequestBody.create("text/xml".toMediaTypeOrNull(), requestBodyText)

        val response = UtilsApi.getAPIService()?.myAppointment(requestBody)

        response?.enqueue(object : Callback<MyAppointmentEnvelope> {
            override fun onResponse(call: Call<MyAppointmentEnvelope>, response: Response<MyAppointmentEnvelope>) {
                val responseEnvelope = response.body()

                if (responseEnvelope != null) {
                    val trackingResult = responseEnvelope.body?.myAppointmentSubBody

                    if (TextUtils.isEmpty(trackingResult?.result)) {
                        Toast.makeText(applicationContext, "Submitted Successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<MyAppointmentEnvelope>, t: Throwable) {
                Log.e("onFailure", t.message)
            }
        })
    }

    private fun validate(): Boolean {

        if (appointmentDate.text.isEmpty()) {
            Toast.makeText(applicationContext, "Date is empty", Toast.LENGTH_SHORT).show()
            return false
        }

        if (customerName.text.isEmpty()) {
            Toast.makeText(applicationContext, "CustomerName is empty", Toast.LENGTH_SHORT).show()
            return false
        }

        if (specialNote.text.isEmpty()) {
            Toast.makeText(applicationContext, "SpecialNote is empty", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
