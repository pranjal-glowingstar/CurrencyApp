package com.apps.currencyapp.data.remote

import com.apps.currencyapp.data.remote.models.CurrencyModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface CurrencyApi {

    @GET("/api/latest.json")
    suspend fun fetchDefaultUSDExchangeRate(
        @QueryMap params: Map<String, String>
    ): Response<CurrencyModel>
}