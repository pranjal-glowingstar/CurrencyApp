package com.apps.currencyapp.presentation.viewmodel

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import com.apps.currencyapp.data.local.entity.CurrencyEntity
import com.apps.currencyapp.data.local.sharedPref.AppConfig
import com.apps.currencyapp.data.remote.models.CurrencyModel
import com.apps.currencyapp.presentation.composables.DispatcherProvider
import com.apps.currencyapp.repository.local.ICurrencyLocalRepository
import com.apps.currencyapp.repository.remote.ICurrencyRemoteRepository
import com.apps.currencyapp.utils.AppConstants
import com.google.gson.Gson
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.ByteArrayInputStream

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private lateinit var mainViewModel: MainViewModel
    private val currencyRemoteRepository = mockk<ICurrencyRemoteRepository>()
    private val currencyLocalRepository = mockk<ICurrencyLocalRepository>()
    private val appConfig = mockk<AppConfig>()
    private val context = mockk<Context>()
    private val assetManager = mockk<AssetManager>()
    private val testDispatcher = StandardTestDispatcher()
    private val gson = mockk<Gson>()

    @Before
    fun setup(){
        mockkObject(DispatcherProvider)
        mockkStatic(Log::class)
        Dispatchers.setMain(testDispatcher)
        every { DispatcherProvider.getIODispatcher() } returns testDispatcher
        every { DispatcherProvider.getDefaultDispatcher() } returns testDispatcher
        val jsonString = "{\"INR\":\"Indian Rupee\",\"JPY\":\"Japanese Yen\",\"USD\":\"Unites States of America Dollar\"}"
        val countryJsonString = "[{\"currencyName\":\"INR\",\"countryName\":\"Indian Rupee\",\"amount\":85.695}]"
        every { context.assets } returns assetManager
        every { assetManager.open(AppConstants.COUNTRY_CODE_MAPPING) } answers { ByteArrayInputStream(jsonString.toByteArray()) }
        every { assetManager.open(AppConstants.DEFAULT_CURRENCY_RATES) } answers { ByteArrayInputStream(countryJsonString.toByteArray()) }

        mainViewModel = MainViewModel(currencyRemoteRepository, currencyLocalRepository, appConfig, context, gson)
        mainViewModel.warmViewModelWithDefaults()

        coEvery { currencyLocalRepository.saveAllCurrencies(any()) } just Runs
        every { appConfig.setLastFetchTimestamp(any()) } just Runs
        every { Log.d(any(), any()) } returns 1
        coEvery { currencyLocalRepository.getAllCurrencies() } returns listOf(CurrencyEntity("INR", "Indian Rupee", 85.5), CurrencyEntity("JPY", "Japanese Yen", 144.4), CurrencyEntity("USD", "United States of America Dollar", 1.0))
    }

    @Test
    fun fetchDefaultRatesThroughApiCall() = runTest {
        coEvery { currencyRemoteRepository.fetchDefaultUSDExchangeRate() } returns Response.success(
            CurrencyModel("", "", 1L, "", mapOf(Pair("INR", 85.5)))
        )
        every { appConfig.getLastFetchTimestamp() } returns -1
        mainViewModel.fetchDefaultUSDExchangeRate()
        advanceUntilIdle()
        assertNotNull(mainViewModel.currencies.value)
        assertEquals(mainViewModel.currencies.value[0].currencyName, "INR")
        assertEquals(mainViewModel.currencies.value[0].amount, 85.5, 0.0)
    }
    @Test
    fun fetchDefaultRatesThroughLocalWithinThirtyMinutes() = runTest {
        every { appConfig.getLastFetchTimestamp() } returns System.currentTimeMillis()
        mainViewModel.fetchDefaultUSDExchangeRate()
        advanceUntilIdle()
        assertNotNull(mainViewModel.currencies.value)
        assertEquals(mainViewModel.currencies.value[1].currencyName, "JPY")
        assertEquals(mainViewModel.currencies.value[1].amount, 144.4, 0.0)
    }
    @Test
    fun fetchDefaultRatesFromAssetsDueToExceptionInNetworkAndNoDataInLocal() = runTest{
        coEvery { currencyRemoteRepository.fetchDefaultUSDExchangeRate() } throws HttpException(Response.error<String>(404, ResponseBody.create(null, "Not Found")))
        every { appConfig.getLastFetchTimestamp() } returns -1
        mainViewModel.fetchDefaultUSDExchangeRate()
        advanceUntilIdle()
        verify(exactly = 1) {
            Log.d(any(), any())
        }
        assertNotNull(mainViewModel.currencies.value)
        assertEquals(mainViewModel.currencies.value[1].currencyName, "JPY")
        assertEquals(mainViewModel.currencies.value[1].amount, 144.4, 0.0)
    }
    @Test
    fun selectCountryAsJapanAndCheckNewExchangeRate() = runTest {
        fetchDefaultRatesThroughApiCall()
        mainViewModel.onCurrencySelected("JPY")
        advanceUntilIdle()
        assertNotNull(mainViewModel.currencies.value)
        assertEquals(mainViewModel.currencies.value[0].amount, 0.59, 0.1)
        assertEquals(mainViewModel.currencies.value[2].amount, 0.006, 0.1)
    }
    @Test
    fun selectCountryAsJapanAndCheckNewExchangeRateByAddingSeventeenYen() = runTest{
        fetchDefaultRatesThroughApiCall()
        selectCountryAsJapanAndCheckNewExchangeRate()
        mainViewModel.updateAmount("17")
        advanceUntilIdle()
        assertNotNull(mainViewModel.currencies.value)
        assertEquals(mainViewModel.currencies.value[0].amount, 10.10, 0.1)
        assertEquals(mainViewModel.currencies.value[2].amount, 0.11, 0.1)
    }
}