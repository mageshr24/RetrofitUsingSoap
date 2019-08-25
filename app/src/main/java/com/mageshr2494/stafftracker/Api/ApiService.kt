package com.mageshr2494.stafftracker.Api

import com.mageshr2494.stafftracker.model.response.locationTracking.*
import com.mageshr2494.stafftracker.model.response.login.LoginResponseEnvelope
import com.mageshr2494.stafftracker.model.response.salesCallPlannerResponse.SalesCallPlannerEnvelope
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("salesTrack.asmx?op=GetUser")
    fun getUserDetails(@Body body: RequestBody): Call<LoginResponseEnvelope>

    @POST("salesTrack.asmx?op=DailyTrackingSave")
    fun locationTrack(@Body body: RequestBody): Call<LocationTrackEnvelope>

    @POST("salesTrack.asmx?op=SalesCallMaster")
    fun salesCall(@Body body: RequestBody): Call<SalesCallEnvelope>

    @POST("salesTrack.asmx?op=SalesCallPlanner")
    fun salesCallPlanner(@Body body: RequestBody): Call<SalesCallPlannerEnvelope>

    @POST("salesTrack.asmx?op=CustomersAppointment")
    fun myAppointment(@Body body: RequestBody): Call<MyAppointmentEnvelope>

    @POST("salesTrack.asmx?op=CustomersMaster")
    fun myTarget(@Body body: RequestBody): Call<MyTargetEnvelope>

}