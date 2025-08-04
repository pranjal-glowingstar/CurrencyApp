package com.apps.currencyapp.repository.local

import com.apps.currencyapp.data.local.entity.CurrencyEntity

interface ICurrencyLocalRepository {

    suspend fun saveAllCurrencies(currencyEntity: List<CurrencyEntity>)
    suspend fun getAllCurrencies(): List<CurrencyEntity>
}