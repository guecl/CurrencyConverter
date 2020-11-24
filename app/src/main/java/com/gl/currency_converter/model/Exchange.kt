package com.gl.currency_converter.model

class Quote(val key: String, val amount: Float) {
    override fun toString(): String {
        return "$key: $amount"
    }
}

class Currency(val key: String, val name: String) {
    override fun toString(): String {
        return "$key : $name"
    }
}