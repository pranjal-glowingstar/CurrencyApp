package com.apps.currencyapp.repository.remote

import com.apps.currencyapp.data.remote.CurrencyApi
import com.apps.currencyapp.data.remote.models.CurrencyModel
import com.apps.currencyapp.utils.AppConstants
import retrofit2.Response
import javax.inject.Inject

class CurrencyRemoteRepositoryImpl @Inject constructor(private val currencyApi: CurrencyApi): ICurrencyRemoteRepository {
    override suspend fun fetchDefaultUSDExchangeRate(): Response<CurrencyModel> {
        return currencyApi.fetchDefaultUSDExchangeRate(getQueryParamMap())
    }
    private fun getQueryParamMap(): Map<String, String>{
        return mutableMapOf<String, String>().apply {
            put(AppConstants.QUERY_BASE, AppConstants.DEFAULT_CURRENCY)
            put(AppConstants.QUERY_APP_ID, AppConstants.APP_ID)
        }
    }
}