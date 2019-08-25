package com.mageshr2494.stafftracker.model.response.locationTracking

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root

@Root(name = "SalesCallMasterResponse")
@Namespace(reference = "http://salestracker.mtcweb.in/")
data class SalesCallSubBody @JvmOverloads constructor(

    @field:Element(name = "SalesCallMasterResult", required = false)
    var result: String? = null

)