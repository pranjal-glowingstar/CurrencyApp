package com.apps.currencyapp.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object DispatcherProvider {

    private val IO: CoroutineDispatcher = Dispatchers.IO
    private val Default: CoroutineDispatcher = Dispatchers.Default

    fun getIODispatcher(): CoroutineDispatcher {
        return IO
    }
    fun getDefaultDispatcher(): CoroutineDispatcher {
        return Default
    }
}