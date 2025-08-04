package com.apps.currencyapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apps.currencyapp.data.local.entity.CurrencyEntity

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllCurrencies(entities: List<CurrencyEntity>)

    @Query("select * from currency")
    suspend fun getAllCurrencies(): List<CurrencyEntity>
}