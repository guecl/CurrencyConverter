package com.gl.currency_converter.di

import android.app.Application
import com.gl.currency_converter.BuildConfig
import com.gl.currency_converter.utils.Constants
import com.gl.currency_converter.utils.getRefreshDate
import com.gl.currency_converter.utils.refreshData
import com.gl.currency_converter.utils.setRefreshDate
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton


@Module
class AppModule {

    companion object {
        const val cacheSize = (5 * 1024 * 1024).toLong()
    }

    @Singleton
    @Provides
    fun provideOkHttpInstance(application: Application): OkHttpClient {

        val cache = Cache(application.cacheDir, cacheSize)
        val okHttpClient = OkHttpClient()
                .newBuilder()
                .cache(cache)
                .addInterceptor { chain ->
                    var request = chain.request()
                    val requestName = request.url.toString()
                    request = if(getRefreshDate(application, requestName).equals(0L) || refreshData(getRefreshDate(application, requestName))) {
                        setRefreshDate(application, requestName, Date().time)
                        request.newBuilder().addHeader("Cache-Control", "public, max-age=" + 30 * 60).build()
                    } else
                        //don't retrieve new data, fetch the cache only
                        request.newBuilder().addHeader("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 30).build()

                    chain.proceed(request)
                }

        if(BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            okHttpClient.addInterceptor(logging)
        }

        return okHttpClient.build()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(client: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}
