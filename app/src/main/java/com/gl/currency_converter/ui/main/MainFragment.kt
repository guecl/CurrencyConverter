package com.gl.currency_converter.ui.main

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.gl.currency_converter.R
import com.gl.currency_converter.di.ViewModelProviderFactory
import com.gl.currency_converter.model.Currency
import com.gl.currency_converter.model.Quote
import com.gl.currency_converter.network.Resource
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainFragment @Inject constructor() : DaggerFragment() {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private lateinit var viewModel: MainViewModel

    @Inject lateinit var exchangeRatesAdapter: ExchangeRatesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, providerFactory).get(MainViewModel::class.java)
        observeEvents()

        go.setOnClickListener {

            val imm = activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)

            displayExchangeRates()
        }

        go.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    displayExchangeRates()
                    true
                }
                else -> false
            }
        }

        currenciesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                val str = amountTv.text.toString()
                if(str.isBlank())
                    return

                displayExchangeRates()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        exchangeRatesGV.adapter = exchangeRatesAdapter

        CoroutineScope(viewModel.viewModelScope.coroutineContext).launch {
            viewModel.fetchCurrencies()
        }

    }


    private fun displayExchangeRates() {
        if(currenciesSpinner.selectedItem == null)
            return
        val str = amountTv.text.toString()
        if(str.isBlank()) {
            exchangeRatesAdapter.quotes = listOf()
            exchangeRatesAdapter.notifyDataSetChanged()
            return
        }

        val currency: Currency = currenciesSpinner.selectedItem as Currency
        try {
            CoroutineScope(viewModel.viewModelScope.coroutineContext).launch {
                viewModel.onAmountChanged(str.toFloat(), currency)
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }

    }

    private fun observeEvents() {

        viewModel.quotes.observe(viewLifecycleOwner) {
             when (it.status) {
                    Resource.Companion.Status.LOADING -> {
                        Log.d("TAG", "LOADING...")
                    }
                    Resource.Companion.Status.SUCCESS -> {
                        it.data?.let { list -> updateExchangeRates(list) }
                    }
                    Resource.Companion.Status.ERROR -> {
                        Log.e("TAG", "ERROR..." + it.message)
                    }
             }
        }

        viewModel.currencies.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Companion.Status.LOADING -> {
                    Log.d("TAG", "LOADING...")
                }
                Resource.Companion.Status.SUCCESS -> {
                    it.data?.let { data -> populateCurrenciesSpinner(data) }
                }
                Resource.Companion.Status.ERROR -> {
                    Log.e("TAG", "ERROR..." + it.message)
                }
            }
        }
    }

    private fun updateExchangeRates(quotes: List<Quote>) {
        exchangeRatesAdapter.quotes = quotes
        exchangeRatesAdapter.notifyDataSetChanged()
    }

    private fun populateCurrenciesSpinner(list: HashMap<String, String>) {
        val entries: List<Currency> = list.toList().map { Currency(it.first, it.second)}.sortedBy { it.name }
        ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, entries).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            currenciesSpinner.adapter = adapter
        }
    }


}