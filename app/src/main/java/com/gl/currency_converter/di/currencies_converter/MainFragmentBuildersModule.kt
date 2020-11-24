package com.gl.currency_converter.di.currencies_converter

import com.gl.currency_converter.ui.main.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeMainFragment() : MainFragment
}