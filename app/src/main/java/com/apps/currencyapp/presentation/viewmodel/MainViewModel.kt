package com.apps.currencyapp.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.currencyapp.data.local.entity.CurrencyEntity
import com.apps.currencyapp.data.local.sharedPref.AppConfig
import com.apps.currencyapp.presentation.composables.DispatcherProvider
import com.apps.currencyapp.repository.local.ICurrencyLocalRepository
import com.apps.currencyapp.repository.remote.ICurrencyRemoteRepository
import com.apps.currencyapp.utils.AppConstants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val currencyRemoteRepository: ICurrencyRemoteRepository,
    private val currencyLocalRepository: ICurrencyLocalRepository,
    private val appConfig: AppConfig,
    @ApplicationContext private val context: Context,
    private val gson: Gson
) : ViewModel() {

    private val _currencies = MutableStateFlow(listOf<CurrencyEntity>())
    private val _amount = MutableStateFlow("")
    private val _selectedCurrency = MutableStateFlow(AppConstants.DEFAULT_CURRENCY)
    private val _shouldShowLoader = MutableStateFlow(false)
    private val _disclaimer = MutableStateFlow(false)
    private val _license = MutableStateFlow(false)
    private var currencyAmountMapping = mutableMapOf<String, Double>()
    private var countryCodeMapping = mutableMapOf<String, String>()

    val currencies = _currencies.asStateFlow()
    val amount = _amount.asStateFlow()
    val selectedCurrency = _selectedCurrency.asStateFlow()
    val shouldShowLoader = _shouldShowLoader.asStateFlow()
    val disclaimer = _disclaimer.asStateFlow()
    val license = _license.asStateFlow()

    fun warmViewModelWithDefaults(){
        viewModelScope.launch(DispatcherProvider.getIODispatcher()) {
            var json = context.assets.open(AppConstants.COUNTRY_CODE_MAPPING).bufferedReader().use { it.readText() }
            var type = object : TypeToken<Map<String, String>>() {}.type
            countryCodeMapping = gson.fromJson(json, type)
            json = context.assets.open(AppConstants.DEFAULT_CURRENCY_RATES).bufferedReader().use { it.readText() }
            type = object : TypeToken<List<CurrencyEntity>>() {}.type
            currencyLocalRepository.saveAllCurrencies(gson.fromJson(json, type))
        }
    }

    fun fetchDefaultUSDExchangeRate() {
        viewModelScope.launch(DispatcherProvider.getIODispatcher()) {
            _shouldShowLoader.value = true
            if (System.currentTimeMillis() - appConfig.getLastFetchTimestamp() >= AppConstants.HALF_HOUR_IN_MILLIS) {
                try {
                    val response = currencyRemoteRepository.fetchDefaultUSDExchangeRate()
                    if (response.isSuccessful) {
                        response.body()?.let { body ->
                            val currencies = body.rates.map { CurrencyEntity(it.key, countryCodeMapping[it.key]!!, it.value) }
                            currencyLocalRepository.saveAllCurrencies(currencies)
                            appConfig.setLastFetchTimestamp(System.currentTimeMillis())
                        }
                    }
                }catch (e: Exception){
                    Log.d("Exception in network call", e.message ?: "")
                }
            }
            val currencyEntity = currencyLocalRepository.getAllCurrencies()
            currencyAmountMapping = currencyEntity.associateBy(keySelector = { it.currencyName }, valueTransform = { it.amount }).toMutableMap()
            _currencies.value = currencyEntity.sortedBy { it.countryName }
            _shouldShowLoader.value = false
        }
    }

    fun updateAmount(value: String) {
        _amount.value = value
        convertCurrencyRatesBasedOnValue(_selectedCurrency.value)
    }

    fun onCurrencySelected(value: String) {
        _selectedCurrency.value = value
        convertCurrencyRatesBasedOnValue(value)
    }

    private fun convertCurrencyRatesBasedOnValue(value: String) {
        viewModelScope.launch(DispatcherProvider.getDefaultDispatcher()) {
            var currentCurrencyList = _currencies.value.toMutableList()
            currentCurrencyList = currentCurrencyList.map {
                CurrencyEntity(
                    it.currencyName,
                    it.countryName,
                    convertAmount(it.currencyName, value)
                )
            }.toMutableList()
            _currencies.value = currentCurrencyList.sortedBy { it.countryName }
        }
    }

    private fun convertAmount(from: String, to: String): Double {
        return (currencyAmountMapping[from]!! / currencyAmountMapping[to]!!) * (if (_amount.value.isEmpty()) 1.0 else _amount.value.toDouble())
    }
    fun onDisclaimerClicked(value: Boolean){
        _disclaimer.value = value
    }
    fun onLicenseClicked(value: Boolean){
        _license.value = value
    }
}