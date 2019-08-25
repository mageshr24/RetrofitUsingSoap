package com.mageshr2494.stafftracker.model.response.locationTracking

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root

@Root(name = "CustomersAppointmentResponse")
@Namespace(reference = "http://salestracker.mtcweb.in/")
data class MyAppointmentSubBody @JvmOverloads constructor(

    @field:Element(name = "CustomersAppointmentResult", required = false)
    var result: String? = null

)