package com.apps.currencyapp.repository.local

import com.apps.currencyapp.data.local.dao.CurrencyDao
import com.apps.currencyapp.data.local.entity.CurrencyEntity
import javax.inject.Inject

class CurrencyLocalRepositoryImpl @Inject constructor(private val currencyDao: CurrencyDao): ICurrencyLocalRepository {
    override suspend fun saveAllCurrencies(currencyEntity: List<CurrencyEntity>) {
        currencyDao.saveAllCurrencies(currencyEntity)
    }

    override suspend fun getAllCurrencies(): List<CurrencyEntity> {
        return currencyDao.getAllCurrencies()
    }
}