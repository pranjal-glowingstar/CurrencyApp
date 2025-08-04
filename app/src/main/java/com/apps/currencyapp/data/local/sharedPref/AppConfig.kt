package com.apps.currencyapp.data.local.sharedPref

import android.content.Context
import android.content.SharedPreferences
import com.apps.currencyapp.utils.AppConstants

class AppConfig(context: Context) {

    private var sharedPrefRead: SharedPreferences = context.getSharedPreferences(AppConstants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
    private var sharedPrefEdit: SharedPreferences.Editor = sharedPrefRead.edit()

    fun getLastFetchTimestamp(): Long{
        return sharedPrefRead.getLong(AppConstants.SharedPrefKeys.LAST_SYNC_TIME, -1)
    }
    fun setLastFetchTimestamp(value: Long){
        sharedPrefEdit.putLong(AppConstants.SharedPrefKeys.LAST_SYNC_TIME, value).commit()
    }
}