package com.mageshr2494.stafftracker.model.response.locationTracking

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root


@Root(name = "Body")
data class MyAppointmentBody @JvmOverloads constructor(

    @field:Element(name = "CustomersAppointmentResponse", required = false)
    var myAppointmentSubBody: MyAppointmentSubBody? = null

)