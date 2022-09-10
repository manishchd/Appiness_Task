package com.appinesstask.networkcalls

import com.appinesstask.models.ContactRes
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitApi {

    @GET(ApiUrls.GET_MY_LIST)
    suspend fun getMyListData(): Response<ContactRes>

}