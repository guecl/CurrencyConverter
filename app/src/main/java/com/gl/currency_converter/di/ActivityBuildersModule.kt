package com.gl.currency_converter.di

import com.gl.currency_converter.MainActivity
import com.gl.currency_converter.di.currencies_converter.CurrencyConverterScope
import com.gl.currency_converter.di.currencies_converter.MainFragmentBuildersModule
import com.gl.currency_converter.di.currencies_converter.MainModule
import com.gl.currency_converter.di.currencies_converter.MainViewModelsModule
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuildersModule {

    @CurrencyConverterScope
    @ContributesAndroidInjector(
        modules = [
            MainViewModelsModule::class,
            MainFragmentBuildersModule::class,
            MainModule::class
        ]
    )
    abstract fun contributeMainActivity(): MainActivity

}