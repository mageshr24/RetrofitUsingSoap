package com.mageshr2494.stafftracker.model.response.login

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root


@Root(name = "Body")
data class LoginResponseBody @JvmOverloads constructor(

    @field:Element(name = "GetUserResponse", required = false)
    var getUserResponse: LoginResponseSubBody? = null

)