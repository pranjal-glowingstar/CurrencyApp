package com.apps.currencyapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.apps.currencyapp.data.local.dao.CurrencyDao
import com.apps.currencyapp.data.local.entity.CurrencyEntity

@Database(entities = [CurrencyEntity::class], version = 1)
abstract class CurrencyDatabase: RoomDatabase() {
    abstract fun provideCurrencyDao(): CurrencyDao
}