package com.mageshr2494.stafftracker.model.response.salesCallPlannerResponse

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root

@Root(name = "SalesCallPlannerResponse")
@Namespace(reference = "http://salestracker.mtcweb.in/")
data class SalesCallPlannerSubBody @JvmOverloads constructor(

    @field:Element(name = "SalesCallPlannerResult", required = false)
    var result: String? = null

)