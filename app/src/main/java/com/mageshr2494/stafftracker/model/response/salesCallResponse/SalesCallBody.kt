package com.mageshr2494.stafftracker.model.response.locationTracking

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root


@Root(name = "Body")
data class SalesCallBody @JvmOverloads constructor(

    @field:Element(name = "SalesCallMasterResponse", required = false)
    var salesCallSubBody: SalesCallSubBody? = null

)