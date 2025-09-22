package com.apps.currencyapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "currency")
@Serializable
data class CurrencyEntity(
    @ColumnInfo("currencyName")
    @PrimaryKey
    val currencyName: String,

    @ColumnInfo("countryName")
    val countryName: String,

    @ColumnInfo("amount")
    val amount: Double
)