package com.gl.currency_converter.di

import android.app.Application
import com.gl.currency_converter.BaseApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ViewModelFactoryModule ::class,
        ActivityBuildersModule::class
    ]
)
interface AppComponent: AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

}