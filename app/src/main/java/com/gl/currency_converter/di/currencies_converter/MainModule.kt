package com.gl.currency_converter.di.currencies_converter


import com.gl.currency_converter.network.MainApi
import com.gl.currency_converter.ui.main.ExchangeRatesAdapter
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit


@Module
class MainModule {

    companion object {

        @CurrencyConverterScope
        @Provides
        fun provideCurrencyLayerApi(retrofit: Retrofit) : MainApi {
            return retrofit.create(MainApi::class.java)
        }

        @CurrencyConverterScope
        @Provides
        fun provideAdapter(): ExchangeRatesAdapter {
            return ExchangeRatesAdapter()
        }
    }

}