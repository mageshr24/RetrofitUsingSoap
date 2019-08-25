package com.mageshr2494.stafftracker.Api

object UtilsApi {

//    val BASE_URL_API = "https://letit.navioindia.com/WebService/"
    val BASE_URL_API = "http://salestracker.mtcweb.in/WebService/"

    fun getAPIService(): ApiService? {
        return APIClient.getClient(BASE_URL_API)?.create(ApiService::class.java)
    }
}
