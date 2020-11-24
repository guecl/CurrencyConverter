package com.gl.currency_converter.model


data class Quotes (
    val success: Boolean,
    val error: Error,
    val terms: String,
    val privacy: String,
    val timestamp: Int,
    val quotes: HashMap<String, Float>
)
