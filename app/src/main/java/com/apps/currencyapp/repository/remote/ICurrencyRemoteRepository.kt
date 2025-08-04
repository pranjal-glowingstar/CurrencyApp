package com.apps.currencyapp.repository.remote

import com.apps.currencyapp.data.remote.models.CurrencyModel
import retrofit2.Response

interface ICurrencyRemoteRepository {

    suspend fun fetchDefaultUSDExchangeRate(): Response<CurrencyModel>
}