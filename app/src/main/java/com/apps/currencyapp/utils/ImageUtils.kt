package com.apps.currencyapp.utils

object ImageUtils {

    private val BASE_URL = "https://flagsapi.com/"

    fun getImageUrlBasedOnCountryCode(code: String): String{
        return "${BASE_URL}${code}/shiny/64.png"
    }
}