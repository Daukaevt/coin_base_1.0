package com.wixsite.mupbam1.resume.coinbase10

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface retrofitApi {
    @GET("/api/v3/coins/markets?vs_currency=usd")
    suspend fun getData(): Response<List<CoinDataStructureItem>>
}