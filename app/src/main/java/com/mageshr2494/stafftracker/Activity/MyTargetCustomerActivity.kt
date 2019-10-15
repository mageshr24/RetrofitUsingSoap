package com.mageshr2494.stafftracker.Activity

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
import com.mageshr2494.stafftracker.model.response.locationTracking.MyTargetEnvelope
import kotlinx.android.synthetic.main.activity_my_target_customer.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyTargetCustomerActivity : AppCompatActivity() {
    lateinit var utils: SharedPreference
    var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_target_customer)

        utils = SharedPreference(applicationContext)
        userId = utils.getUserId()

        //actionbar
        val actionbar = supportActionBar
        actionbar!!.title = "My Target Custormer"
        actionbar.setBackgroundDrawable(ColorDrawable(Color.parseColor("#00000000")))
        actionbar.setDisplayHomeAsUpEnabled(true)

        /*TYPE SPINNER DATA */
        val typeData = arrayOf("Select Type", "Green", "Blue", "Yellow", "Black", "Crimson", "Orange")
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
        val callTypeData = arrayOf("Select CallType", "Green", "Blue", "Yellow", "Black", "Crimson", "Orange")
        val callTypeAdapter = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            callTypeData // Array
        )
        callTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typeSpinner.adapter = callTypeAdapter

        /*CUSTOMER STATUS SPINNER DATA */
        val customerStatusData =
            arrayOf("Select Customer Status", "Green", "Blue", "Yellow", "Black", "Crimson", "Orange")
        val customerStatusAdapter = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            customerStatusData // Array
        )
        customerStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        customerStatusSpinner.adapter = customerStatusAdapter

        /*CURRENT STATUS SPINNER DATA */
        val currentStatusData =
            arrayOf("Select Customer Status", "Green", "Blue", "Yellow", "Black", "Crimson", "Orange")
        val currentStatusAdapter = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            currentStatusData // Array
        )
        currentStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        currentStatus.adapter = currentStatusAdapter

        salesCallBtn.setOnClickListener {

            if (validate()) {

                sendMyTargetCustormer(
                    customerName.text.toString(),
                    typeSpinner.selectedItem.toString(),
                    customerStatusSpinner.selectedItem.toString(),
                    boardOfDirector.text.toString(),
                    website.text.toString(),
                    phone.text.toString(),
                    city.text.toString(),
                    fax.text.toString(),
                    contactPerson.text.toString(),
                    designation.text.toString(),
                    mobilNumber.text.toString(),
                    emailId.text.toString(),
                    currentStatus.selectedItem.toString(),
                    remarks.text.toString()
                )

            }
        }
    }

    private fun validate(): Boolean {

        if (customerName.text.isEmpty()) {
            Toast.makeText(applicationContext, "CustomerName is empty", Toast.LENGTH_SHORT).show()
            return false
        }

        if (typeSpinner.selectedItem.toString().equals("Select Type")) {
            Toast.makeText(this, "Please select Type", Toast.LENGTH_SHORT).show()
            return false
        }

        if (customerStatusSpinner.selectedItem.toString().equals("Select Type")) {
            Toast.makeText(this, "Please select customer status", Toast.LENGTH_SHORT).show()
            return false
        }

        if (boardOfDirector.text.isEmpty()) {
            Toast.makeText(applicationContext, "Board of Director is empty", Toast.LENGTH_SHORT).show()
            return false
        }

        if (website.text.isEmpty()) {
            Toast.makeText(applicationContext, "Website is empty", Toast.LENGTH_SHORT).show()
            return false
        }

        if (phone.text.isEmpty()) {
            Toast.makeText(applicationContext, "Phone Number is empty", Toast.LENGTH_SHORT).show()
            return false
        }

        if (city.text.isEmpty()) {
            Toast.makeText(applicationContext, "City is empty", Toast.LENGTH_SHORT).show()
            return false
        }

        if (fax.text.isEmpty()) {
            Toast.makeText(applicationContext, "Fax is empty", Toast.LENGTH_SHORT).show()
            return false
        }

        if (contactPerson.text.isEmpty()) {
            Toast.makeText(applicationContext, "Contact Person is empty", Toast.LENGTH_SHORT).show()
            return false
        }

        if (designation.text.isEmpty()) {
            Toast.makeText(applicationContext, "Designation is empty", Toast.LENGTH_SHORT).show()
            return false
        }

        if (mobilNumber.text.isEmpty()) {
            Toast.makeText(applicationContext, "Mobile Number is empty", Toast.LENGTH_SHORT).show()
            return false
        }

        if (emailId.text.isEmpty()) {
            Toast.makeText(applicationContext, "Email Id is empty", Toast.LENGTH_SHORT).show()
            return false
        }

        if (currentStatus.selectedItem.toString().equals("Select current status")) {
            Toast.makeText(applicationContext, "Select current status", Toast.LENGTH_SHORT).show()
            return false
        }

        if (remarks.text.isEmpty()) {
            Toast.makeText(applicationContext, "Remarks is empty", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
/*
                    customerName.text.toString(),
                    typeSpinner.selectedItem.toString(),
                    customerStatusSpinner.selectedItem.toString(),
                    boardOfDirector.text.toString(),
                    website.text.toString(),
                    phone.text.toString(),
                    city.text.toString(),
                    fax.text.toString(),
                    contactPerson.text.toString(),
                    designation.text.toString(),
                    mobilNumber.text.toString(),
                    emailId.text.toString(),
                    currentStatus.selectedItem.toString(),
                    remarks.text.toString()*/

    private fun sendMyTargetCustormer(
        customerName: String,
        typeSpinner: String,
        customerStatusSpinner: String,
        boardOfDirector: String,
        website: String,
        phone: String,
        city: String,
        fax: String,
        contactPerson: String,
        designation: String,
        mobilNumber: String,
        emailId: String,
        currentStatus: String,
        remarks: String
    ) {

        val requestBodyText = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "  <soap12:Body>\n" +
                " <CustomersMaster xmlns=\"http://salestracker.mtcweb.in/\">\n " +
                "     <BussData> \n" +
                "       <UserID>" + userId + "</UserID>\n" +
                "       <CustName>" + customerName + "</CustName> \n" +
                "       <TypeID>" + typeSpinner + "</TypeID> \n" +
                "       <CmpTypeID>" + customerStatusSpinner + "</CmpTypeID> \n" +
                "       <BoardOfDirector>" + boardOfDirector + "</BoardOfDirector> \n" +
                "       <WebSite>" + website + "</WebSite> \n" +
                "       <ContactNo>" + phone + "</ContactNo> \n" +
                "       <City>" + city + "</City> \n" +
                "       <Fax>" + fax + "</Fax> \n" +
                "       <ContactPerson>" + contactPerson + "</ContactPerson> \n" +
                "       <Designation>" + designation + "</Designation> \n" +
                "       <ContactNo>" + mobilNumber + "</ContactNo> \n" +
                "       <EmailID>" + emailId + "</EmailID> \n" +
                "       <StatusID>" + currentStatus + "</StatusID> \n" +
                "       <Remarks>" + remarks + "</Remarks> \n" +
                "     </BussData>\n" +
                "    </CustomersMaster> \n" +
                "  </soap12:Body>\n" +
                "</soap12:Envelope>"

        val requestBody = RequestBody.create("text/xml".toMediaTypeOrNull(), requestBodyText)

        val response = UtilsApi.getAPIService()?.myTarget(requestBody)

        response?.enqueue(object : Callback<MyTargetEnvelope> {
            override fun onResponse(call: Call<MyTargetEnvelope>, response: Response<MyTargetEnvelope>) {
                val responseEnvelope = response.body()

                if (responseEnvelope != null) {
                    val trackingResult = responseEnvelope!!.body?.myTargetSubBody

                    if (TextUtils.isEmpty(trackingResult?.result)) {
                        Log.v("trackingResult", "trackingResult")
                        Toast.makeText(applicationContext, "Submitted Successfully", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<MyTargetEnvelope>, t: Throwable) {
                Log.e("onFailure", t.message)
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
