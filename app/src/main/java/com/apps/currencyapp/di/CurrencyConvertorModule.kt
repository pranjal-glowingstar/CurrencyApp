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
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CurrencyConvertorModule {

    @Provides
    @Singleton
    fun providesCurrencyDatabase(@ApplicationContext context: Context): CurrencyDatabase{
        return Room.databaseBuilder(context, CurrencyDatabase::class.java, "currency_database").build()
    }

    @Provides
    @Singleton
    fun providesCurrencyDao(database: CurrencyDatabase): CurrencyDao{
        return database.provideCurrencyDao()
    }

    @Provides
    @Singleton
    fun providesCurrencyApi(): CurrencyApi{
        return Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(CurrencyApi::class.java)
    }

    @Provides
    @Singleton
    fun providesAppConfig(@ApplicationContext context: Context): AppConfig{
        return AppConfig(context)
    }

    @Provides
    @Singleton
    fun providesGson(): Gson{
        return Gson()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class CurrencyConvertorInterfaceModule{

    @Binds
    @Singleton
    abstract fun providesCurrencyLocalRepository(currencyLocalRepositoryImpl: CurrencyLocalRepositoryImpl): ICurrencyLocalRepository

    @Binds
    @Singleton
    abstract fun providesCurrencyRemoteRepository(currencyRemoteRepositoryImpl: CurrencyRemoteRepositoryImpl): ICurrencyRemoteRepository
}