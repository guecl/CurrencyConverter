package com.gl.currency_converter.utils

import android.content.Context
import java.util.*

fun getRefreshDate(context: Context, requestName: String): Long {
    val sharedPref = context.getSharedPreferences("APP_PREFERENCE", Context.MODE_PRIVATE)
    val defaultValue = 0L
    return sharedPref.getLong("refresh-date $requestName", defaultValue)
}

fun setRefreshDate(context: Context, requestName: String, time: Long) {
    val sharedPref = context.getSharedPreferences("APP_PREFERENCE", Context.MODE_PRIVATE)
    with (sharedPref.edit()) {
        putLong("refresh-date $requestName", time)
        apply()
    }
}

fun refreshData(lastRefreshTime: Long): Boolean {
    return (Date().time - lastRefreshTime) > 30 * 60 * 1000
}