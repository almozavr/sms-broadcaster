package ua.sprotyv.smsbroadcaster.feature.fetcher.data

import retrofit2.http.GET
import retrofit2.http.Header
import ua.sprotyv.smsbroadcaster.feature.fetcher.data.entity.ApiBroadcast

interface BroadcastApiService {

    @GET("/sms-broadcast")
    suspend fun fetchBroadcast(@Header("Authorization") token: String): ApiBroadcast
}
