package com.mageshr2494.stafftracker.model.response.locationTracking

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "Body")
data class LocationTrackBody @JvmOverloads constructor(

    @field:Element(name = "DailyTrackingSaveResponse", required = false)
    var dailyTrackingSaveResponse: LocationTrackSubBody? = null

)