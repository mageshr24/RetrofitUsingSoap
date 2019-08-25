package com.mageshr2494.stafftracker.model.response.locationTracking

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root

@Root(name = "CustomersMasterResponse")
@Namespace(reference = "http://salestracker.mtcweb.in/")
data class MyTargetSubBody @JvmOverloads constructor(

    @field:Element(name = "CustomersMasterResult", required = false)
    var result: String? = null

)