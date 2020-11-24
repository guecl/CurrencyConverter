package com.gl.currency_converter.di.currencies_converter

import androidx.lifecycle.ViewModel
import com.gl.currency_converter.di.ViewModelKey
import com.gl.currency_converter.ui.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel) : ViewModel


}