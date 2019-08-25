package com.mageshr2494.stafftracker.model.response.login

import com.mageshr2494.loginretrofit.model.response.login.LoginResponseData
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "GetUserResult")
data class LoginResponseModel @JvmOverloads constructor(

    @field:Element(name = "SalesTracker")
    var FFDTypes: LoginResponseData? = null

)