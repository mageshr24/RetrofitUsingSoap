package com.mageshr2494.loginretrofit.model.response.login

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "SalesTracker")
data class LoginResponseData constructor(

    @field:Element(name = "ID", required = false)
    var ID: Int = 0,

    @field:Element(name = "DOB", required = false)
    var DOB: String? = null,

    @field:Element(name = "Gender", required = false)
    var Gender: String? = null,

    @field:Element(name = "MartialStatus", required = false)
    var MartialStatus: String? = null,

    @field:Element(name = "LocationID", required = false)
    var LocationID: String? = null,

    @field:Element(name = "DesignationID", required = false)
    var DesignationID: String? = null,

    @field:Element(name = "DOJ", required = false)
    var DOJ: String? = null,

    @field:Element(name = "TypeID", required = false)
    var TypeID: String? = null,

    @field:Element(name = "SalesCallDt", required = false)
    var SalesCallDt: String? = null,

    @field:Element(name = "CustOrVendorID", required = false)
    var CustOrVendorID: String? = null,

    @field:Element(name = "CallTypeID", required = false)
    var CallTypeID: String? = null,

    @field:Element(name = "SupportedByID", required = false)
    var SupportedByID: String? = null,

    @field:Element(name = "UserID", required = false)
    var UserID: Int? = null,

    @field:Element(name = "GeoLocID", required = false)
    var GeoLocID: String? = null,

    @field:Element(name = "StatusID", required = false)
    var StatusID: String? = null,

    @field:Element(name = "UserName", required = false)
    var UserName: String? = null,

    @field:Element(name = "CmpTypeID", required = false)
    var CmpTypeID: String? = null,

    @field:Element(name = "Success", required = false)
    var Success: String? = null,

    @field:Element(name = "CargoTypeID", required = false)
    var CargoTypeID: String? = null,

    @field:Element(name = "TradeID", required = false)
    var TradeID: String? = null,

    @field:Element(name = "ShipmentModeID", required = false)
    var ShipmentModeID: String? = null,

    @field:Element(name = "CurrentStatusID", required = false)
    var CurrentStatusID: String? = null,

    @field:Element(name = "CustID", required = false)
    var CustID: String? = null,

    @field:Element(name = "AppDate", required = false)
    var AppDate: String? = null

)
