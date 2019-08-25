package com.mageshr2494.stafftracker.model.response.locationTracking

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root

@Root(name = "DailyTrackingSaveResponse")
@Namespace(reference = "http://salestracker.mtcweb.in/")
data class LocationTrackSubBody @JvmOverloads constructor(

    @field:Element(name = "DailyTrackingSaveResult", required = false)
    var result: String? = null

)