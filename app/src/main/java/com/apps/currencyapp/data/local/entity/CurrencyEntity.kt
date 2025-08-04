package com.apps.currencyapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class CurrencyEntity(
    @ColumnInfo("currencyName")
    @PrimaryKey
    val currencyName: String,

    @ColumnInfo("countryName")
    val countryName: String,

    @ColumnInfo("amount")
    val amount: Double
)