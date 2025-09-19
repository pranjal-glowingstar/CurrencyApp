package com.apps.currencyapp.presentation.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.currencyapp.data.local.entity.CurrencyEntity
import com.apps.currencyapp.data.local.sharedPref.AppConfig
import com.apps.currencyapp.repository.local.ICurrencyLocalRepository
import com.apps.currencyapp.repository.remote.ICurrencyRemoteRepository
import com.apps.currencyapp.utils.AppConstants
import com.apps.currencyapp.utils.DispatcherProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
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
    private val _shouldShowLoader = MutableStateFlow(false)
    private val _footerAction: MutableSharedFlow<FooterAction?> = MutableSharedFlow()
    private val _currency1 =
        MutableStateFlow(CurrencySelectionModel(AppConstants.DEFAULT_CURRENCY_1, ""))
    private val _currency2 =
        MutableStateFlow(CurrencySelectionModel(AppConstants.DEFAULT_CURRENCY_2, ""))
    private var currencyAmountMapping = mutableMapOf<String, Double>()
    private var countryCodeMapping = mutableMapOf<String, String>()

    val currencies = _currencies.asStateFlow()
    val footerAction = _footerAction.asSharedFlow()
    val currency1 = _currency1.asStateFlow()
    val currency2 = _currency2.asStateFlow()

    init {
        viewModelScope.launch(DispatcherProvider.getIODispatcher()) {
            var json = context.assets.open(AppConstants.COUNTRY_CODE_MAPPING).bufferedReader()
                .use { it.readText() }
            var type = object : TypeToken<Map<String, String>>() {}.type
            countryCodeMapping = gson.fromJson(json, type)
            json = context.assets.open(AppConstants.DEFAULT_CURRENCY_RATES).bufferedReader()
                .use { it.readText() }
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
                            val currencies = body.rates.map {
                                CurrencyEntity(
                                    it.key,
                                    countryCodeMapping[it.key]!!,
                                    it.value
                                )
                            }
                            currencyLocalRepository.saveAllCurrencies(currencies)
                            appConfig.setLastFetchTimestamp(System.currentTimeMillis())
                        }
                    }
                } catch (e: Exception) {
                    Log.d("Exception in network call", e.message ?: "")
                }
            }
            val currencyEntity = currencyLocalRepository.getAllCurrencies()
            currencyAmountMapping = currencyEntity.associateBy(
                keySelector = { it.currencyName },
                valueTransform = { it.amount }).toMutableMap()
            _currencies.value = currencyEntity.sortedBy { it.countryName }
            _shouldShowLoader.value = false
        }
    }

    fun updateAmount(value: String, type: CurrencyInputType) {
        if (type == CurrencyInputType.CURRENCY_1) {
            _currency1.value = _currency1.value.copy(currentAmount = value)
            _currency2.value = _currency2.value.copy(
                currentAmount = getConvertedAmount(
                    _currency2.value.currencyName,
                    _currency1.value.currencyName,
                    _currency1.value.currentAmount
                )
            )
        } else {
            _currency2.value = _currency2.value.copy(currentAmount = value)
            _currency1.value = _currency1.value.copy(
                currentAmount = getConvertedAmount(
                    _currency1.value.currencyName,
                    _currency2.value.currencyName,
                    _currency2.value.currentAmount
                )
            )
        }
    }
    fun onCurrencySelected(selectedCurrency: String, currencyInputType: CurrencyInputType) {
        if (currencyInputType == CurrencyInputType.CURRENCY_1) {
            _currency1.value = _currency1.value.copy(currencyName = selectedCurrency)
            _currency2.value = _currency2.value.copy(
                currentAmount = getConvertedAmount(
                    _currency2.value.currencyName,
                    _currency1.value.currencyName,
                    _currency1.value.currentAmount
                )
            )
        } else {
            _currency2.value = _currency2.value.copy(currencyName = selectedCurrency)
            _currency1.value = _currency1.value.copy(
                currentAmount = getConvertedAmount(
                    _currency1.value.currencyName,
                    _currency2.value.currencyName,
                    _currency2.value.currentAmount
                )
            )
        }
    }

    @SuppressLint("DefaultLocale")
    private fun getConvertedAmount(currencyA: String, currencyB: String, total: String): String {
        if(total == AppConstants.EMPTY){
            return total
        }
        val result = ((currencyAmountMapping[currencyA]!! / currencyAmountMapping[currencyB]!!) * (total.toDouble()))
        return String.format("%.3f", result)
    }

    fun onDisclaimerClicked() {
        viewModelScope.launch {
            _footerAction.emit(FooterAction.DISCLAIMER)
        }
    }
    fun onLicenseClicked() {
        viewModelScope.launch {
            _footerAction.emit(FooterAction.LICENSE)
        }
    }
    fun getLastUpdatedTime(): String {
        val instant = Instant.ofEpochMilli(appConfig.getLastFetchTimestamp())
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return localDateTime.format(formatter)
    }
    fun onCurrencySwapped(){
        _currency1.value = _currency2.value.also { _currency2.value = _currency1.value }
    }
    fun refreshRates(){
        appConfig.setLastFetchTimestamp(-1)
        fetchDefaultUSDExchangeRate()
    }
}

enum class CurrencyInputType {
    CURRENCY_1,
    CURRENCY_2
}
enum class FooterAction{
    DISCLAIMER,
    LICENSE
}
data class CurrencySelectionModel(val currencyName: String, val currentAmount: String)