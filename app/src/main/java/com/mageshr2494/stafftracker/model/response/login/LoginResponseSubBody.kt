package com.mageshr2494.stafftracker.model.response.login

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root


@Root(name = "GetUserResponse")
@Namespace(reference = "http://salestracker.mtcweb.in/")
data class LoginResponseSubBody @JvmOverloads constructor(

    @field:Element(name = "GetUserResult")
    var result: LoginResponseModel? = null

)