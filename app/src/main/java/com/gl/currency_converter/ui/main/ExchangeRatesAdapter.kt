package com.gl.currency_converter.ui.main

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.gl.currency_converter.model.Quote


class ExchangeRatesAdapter(var quotes: List<Quote> = listOf()): BaseAdapter() {

    override fun getCount(): Int {
       return quotes.size
    }

    override fun getItem(position: Int): Quote {
        return quotes[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val itemTextView = TextView(parent?.context)
        val quote = quotes[position]
        itemTextView.text = quote.toString()
        return itemTextView
    }
}