package com.mageshr2494.stafftracker.model.response.salesCallPlannerResponse

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "Body")
data class SalesCallPlannerBody @JvmOverloads constructor(

    @field:Element(name = "SalesCallPlannerResponse", required = false)
    var salesCallPlannerSubBody: SalesCallPlannerSubBody? = null

)