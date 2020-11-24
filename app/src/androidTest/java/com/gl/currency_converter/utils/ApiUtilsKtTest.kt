package com.gl.currency_converter.utils

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import java.util.*

class ApiUtilsKtTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun refreshDateTest() {
        val time = Date().time
        setRefreshDate(context, "req1", time)
        assertEquals(time, getRefreshDate(context, "req1"))
    }

    @Test
    fun refreshDataTest() {
        var time = Date().time - 31 * 60 * 1000 // after 31 min
        assertEquals(true, refreshData(time))
        time = Date().time - 5 * 60 * 1000 // after 5 min
        assertEquals(false, refreshData(time))
    }

}