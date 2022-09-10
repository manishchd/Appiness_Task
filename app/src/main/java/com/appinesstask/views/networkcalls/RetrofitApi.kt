package com.appinesstask.views.networkcalls

import com.appinesstask.views.models.ContactRes
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitApi {

    @GET(ApiUrls.GET_MY_LIST)
    suspend fun getMyListData(): Response<ContactRes>

}