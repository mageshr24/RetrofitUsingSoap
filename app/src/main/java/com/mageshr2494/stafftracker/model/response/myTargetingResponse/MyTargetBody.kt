package com.mageshr2494.stafftracker.model.response.locationTracking

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "Body")
data class MyTargetBody @JvmOverloads constructor(

    @field:Element(name = "CustomersMasterResponse", required = false)
    var myTargetSubBody: MyTargetSubBody? = null

)