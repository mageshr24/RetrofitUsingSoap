package com.mageshr2494.stafftracker.Activity

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.mageshr2494.stafftracker.Api.UtilsApi
import com.mageshr2494.stafftracker.R
import com.mageshr2494.stafftracker.utils.SharedPreference
import com.mageshr2494.stafftracker.model.response.locationTracking.SalesCallEnvelope
import kotlinx.android.synthetic.main.activity_sales_call.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class SalesCallActivity : AppCompatActivity() {
    lateinit var utils: SharedPreference
    var userId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sales_call)

        utils = SharedPreference(applicationContext)
        userId = utils.getUserId()

        //actionbar
        val actionbar = supportActionBar
        actionbar!!.title = "Sales Call"
        actionbar.setBackgroundDrawable(ColorDrawable(Color.parseColor("#00000000")))
        actionbar.setDisplayHomeAsUpEnabled(true)

        saleCallDate.setOnClickListener {
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
                    saleCallDate.setText(sdf.format(c.getTime()))

                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }

        /*TYPE SPINNER DATA */
        val typeData = arrayOf("Select Type", "Yes", "No")
        val typeDataAdapter = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            typeData // Array
        )
        typeDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typeSpinner.adapter = typeDataAdapter

        /*typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//                text_view.text = "Spinner selected : ${parent.getItemAtPosition(position).toString()}"
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }*/

        /*CALLTYPE SPINNER DATA */
        val callTypeData = arrayOf("Select CallType", "Yes", "No")
        val callTypeAdapter = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            callTypeData // Array
        )
        callTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        callTypeSpinner.adapter = callTypeAdapter


        /*JOIN WITH CALL SPINNER DATA */
        val joinCallWithData = arrayOf("Select Join Call with", "Yes", "No")
        val joinCallWithAdapter = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            joinCallWithData // Array
        )
        joinCallWithAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        joinCallWithSpinner.adapter = joinCallWithAdapter

        /*JOIN WITH CALL SPINNER DATA */
        val customerStatusData = arrayOf("Select Customer Status", "Yes", "No")
        val customerStatusAdapter = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            customerStatusData // Array
        )
        customerStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        customerStatusSpinner.adapter = customerStatusAdapter

        salesCallBtn.setOnClickListener {

            if (validate()) {

                sendSalesCall(
                    typeSpinner.selectedItem.toString(),
                    saleCallDate.text.toString(),
                    callTypeSpinner.selectedItem.toString(),
                    opportunities.text.toString(),
                    joinCallWithSpinner.selectedItem.toString(),
                    customerStatusSpinner.selectedItem.toString()
                )

            }
        }
    }

    private fun validate(): Boolean {

        if (typeSpinner.selectedItem.toString().equals("Select Type")) {
            Toast.makeText(this, "Please select Type", Toast.LENGTH_SHORT).show()
            return false
        }

        if (saleCallDate.text.isEmpty()) {
            Toast.makeText(applicationContext, "Date is empty", Toast.LENGTH_SHORT).show()
            return false
        }

        if (callTypeSpinner.selectedItem.toString().equals("Select CallType")) {
            Toast.makeText(this, "Please select CallType", Toast.LENGTH_SHORT).show()
            return false
        }

        if (opportunities.text.isEmpty()) {
            Toast.makeText(applicationContext, "Opportunities is empty", Toast.LENGTH_SHORT).show()
            return false
        }

        if (typeSpinner.selectedItem.toString().equals("Select Join Call with")) {
            Toast.makeText(this, "Please select joinCallWith", Toast.LENGTH_SHORT).show()
            return false
        }

        if (typeSpinner.selectedItem.toString().equals("Select Customer Status")) {
            Toast.makeText(this, "Please select customerStatus", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }


    private fun sendSalesCall(
        typeSpinner: String,
        saleCallDate: String,
        callTypeSpinner: String,
        opportunities: String,
        joinCallWithSpinner: String,
        customerStatusSpinner: String
    ) {

        val requestBodyText = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "  <soap12:Body>\n" +
                " <SalesCallMaster xmlns=\"http://salestracker.mtcweb.in/\">\n " +
                "     <BussData> \n" +
                "       <UserID>" + userId + "</UserID>\n" +
                "       <TypeID>" + typeSpinner + "</TypeID> \n" +
                "       <SalesCallDt>" + saleCallDate + "</SalesCallDt> \n" +
                "       <CallTypeID>" + callTypeSpinner + "</CallTypeID> \n" +
                "       <OpportunitiesDisscussed>" + opportunities + "</OpportunitiesDisscussed> \n" +
                "       <StatusID>" + customerStatusSpinner + "</StatusID> \n" +
                "     </BussData>\n" +
                "    </SalesCallMaster> \n" +
                "  </soap12:Body>\n" +
                "</soap12:Envelope>"

        val requestBody = RequestBody.create("text/xml".toMediaTypeOrNull(), requestBodyText)

        val response = UtilsApi.getAPIService()?.salesCall(requestBody)

        response?.enqueue(object : Callback<SalesCallEnvelope> {
            override fun onResponse(call: Call<SalesCallEnvelope>, response: Response<SalesCallEnvelope>) {
                val responseEnvelope = response.body()

                if (responseEnvelope != null) {
                    val trackingResult = responseEnvelope!!.body?.salesCallSubBody

                    if (TextUtils.isEmpty(trackingResult?.result)) {
                        Toast.makeText(applicationContext, "Submitted Successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<SalesCallEnvelope>, t: Throwable) {
                Log.e("onFailure", t.message)
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
