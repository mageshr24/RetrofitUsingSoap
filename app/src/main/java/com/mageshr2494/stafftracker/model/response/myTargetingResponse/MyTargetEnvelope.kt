package com.mageshr2494.stafftracker.model.response.locationTracking

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.NamespaceList
import org.simpleframework.xml.Root

@Root(name = "soapenv:Envelope")
@NamespaceList(
    Namespace(reference = "http://www.w3.org/2001/XMLSchema-instance", prefix = "xsi"),
    Namespace(reference = "http://www.w3.org/2001/XMLSchema", prefix = "xsd"),
    Namespace(reference = "http://schemas.xmlsoap.org/soap/encoding/", prefix = "enc"),
    Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soapenv")
)
data class MyTargetEnvelope @JvmOverloads constructor(

    @field:Element(name = "Body")
    @Namespace(prefix = "soap")
    var body: MyTargetBody? = null

)