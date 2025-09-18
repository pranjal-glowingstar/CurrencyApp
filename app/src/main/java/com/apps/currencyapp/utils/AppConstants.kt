package com.apps.currencyapp.utils

object AppConstants {

    const val BASE_URL = "https://openexchangerates.org"
    const val SHARED_PREF_NAME = "currency"
    const val DEFAULT_CURRENCY = "USD"
    const val HALF_HOUR_IN_MILLIS = 1800000
    const val CHUNCKED_CURRENCIES_LENGTH = 2
    const val COUNTRY_CODE_MAPPING = "CountryCodeMapping.json"
    const val DEFAULT_CURRENCY_RATES = "CurrencyRate[30-07-2025].json"
    const val QUERY_BASE = "base"
    const val QUERY_APP_ID = "app_id"
    const val APP_ID = "60c6a50a8f1e43dda14e2b3eeb770ee5"
    const val DISCLAIMER_URL = "https://openexchangerates.org/terms/"
    const val LICENSE_URL = "https://openexchangerates.org/license/"
    const val DEFAULT_CURRENCY_1 = "INR"
    const val DEFAULT_CURRENCY_2 = "USD"

    object SharedPrefKeys{
        const val LAST_SYNC_TIME = "last_sync_time"
    }
}