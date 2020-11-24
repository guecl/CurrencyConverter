package com.gl.currency_converter.model

 data class Currencies (
    var success: Boolean,
    val error: Error,
    var terms: String,
    var privacy: String,
    var currencies: HashMap<String, String>
 )