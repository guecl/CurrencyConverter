package com.gl.currency_converter.network


import com.gl.currency_converter.model.Currencies
import com.gl.currency_converter.model.Quotes
import com.gl.currency_converter.utils.Constants.Companion.ACCESS_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MainApi {
    
    @GET("list")
    suspend fun getCurrencies(@Query("access_key", encoded = true) accessKey: String): Response<Currencies>

    @GET("live")
    suspend fun getQuotes(@Query("access_key", encoded = true) accessKey: String): Response<Quotes>
}