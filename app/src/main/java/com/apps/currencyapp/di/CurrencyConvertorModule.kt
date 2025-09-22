package com.apps.currencyapp.di

import android.content.Context
import androidx.room.Room
import com.apps.currencyapp.data.local.CurrencyDatabase
import com.apps.currencyapp.data.local.dao.CurrencyDao
import com.apps.currencyapp.data.local.sharedPref.AppConfig
import com.apps.currencyapp.data.remote.CurrencyApi
import com.apps.currencyapp.repository.local.CurrencyLocalRepositoryImpl
import com.apps.currencyapp.repository.local.ICurrencyLocalRepository
import com.apps.currencyapp.repository.remote.CurrencyRemoteRepositoryImpl
import com.apps.currencyapp.repository.remote.ICurrencyRemoteRepository
import com.apps.currencyapp.utils.AppConstants
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CurrencyConvertorModule {

    @Provides
    @Singleton
    fun providesCurrencyDatabase(@ApplicationContext context: Context): CurrencyDatabase {
        return Room.databaseBuilder(context, CurrencyDatabase::class.java, "currency_database")
            .build()
    }

    @Provides
    @Singleton
    fun providesCurrencyDao(database: CurrencyDatabase): CurrencyDao {
        return database.provideCurrencyDao()
    }

    @Provides
    @Singleton
    fun providesCurrencyApi(): CurrencyApi {
        val json = Json {
            ignoreUnknownKeys = true
            prettyPrint = false
        }
        return Retrofit.Builder().baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType())).build()
            .create(CurrencyApi::class.java)
    }

    @Provides
    @Singleton
    fun providesAppConfig(@ApplicationContext context: Context): AppConfig {
        return AppConfig(context)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class CurrencyConvertorInterfaceModule {

    @Binds
    @Singleton
    abstract fun providesCurrencyLocalRepository(currencyLocalRepositoryImpl: CurrencyLocalRepositoryImpl): ICurrencyLocalRepository

    @Binds
    @Singleton
    abstract fun providesCurrencyRemoteRepository(currencyRemoteRepositoryImpl: CurrencyRemoteRepositoryImpl): ICurrencyRemoteRepository
}