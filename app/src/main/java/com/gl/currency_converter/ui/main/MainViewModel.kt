package com.gl.currency_converter.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gl.currency_converter.model.Currencies
import com.gl.currency_converter.model.Currency
import com.gl.currency_converter.model.Quote
import com.gl.currency_converter.model.Quotes
import com.gl.currency_converter.network.MainApi
import com.gl.currency_converter.network.Resource
import com.gl.currency_converter.utils.Constants.Companion.ACCESS_KEY
import retrofit2.Response
import javax.inject.Inject

class MainViewModel @Inject constructor(private val apiService: MainApi): ViewModel() {

    private var _currencies: MutableLiveData<Resource<HashMap<String, String>>> = MutableLiveData(Resource.loading())
    val currencies: MutableLiveData<Resource<HashMap<String, String>>> = _currencies

    private var _quotes: MutableLiveData<Resource<List<Quote>>> = MutableLiveData(Resource.loading())
    val quotes: MutableLiveData<Resource<List<Quote>>> = _quotes

    suspend fun fetchCurrencies() {

       _currencies.value = Resource.loading()

        try{
            val response : Response<Currencies> = apiService.getCurrencies(ACCESS_KEY)
            if (response.isSuccessful && response.body()!= null) {
                val body: Currencies = response.body()!!
                if (body.success)
                    _currencies.value = Resource.success(body.currencies)
                else {
                    _currencies.value = Resource.error(body.error.info)
                }
            } else
                _currencies.value = Resource.error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            e.printStackTrace()
            _currencies.value = Resource.error(e.message ?: e.toString())
        }

    }

    suspend fun onAmountChanged(amount: Float, currencySelected: Currency) {

        _quotes.value = Resource.loading()

        try{

            val response = apiService.getQuotes(ACCESS_KEY)
                if (response.isSuccessful && response.body()!= null) {
                    val body: Quotes = response.body()!!
                    if (body.success) {
                        val calculatedAmount = calculateQuotes(amount, currencySelected, body.quotes)
                        _quotes.value = Resource.success(calculatedAmount)
                    } else {
                        _quotes.value = Resource.error(body.error.info)
                    }
                } else
                    _quotes.value = Resource.error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            e.printStackTrace()
            _currencies.value = Resource.error(e.message ?: e.toString())
        }

    }

    private fun calculateQuotes(amount: Float, currency: Currency, quotes: HashMap<String, Float>): List<Quote> {
        val key = "USD${currency.key}"
        val q = quotes[key]
        return quotes.map {
            val converted = if(q == null) 0f else amount * it.value/q
            val currencyKey = it.key.replaceFirst("USD", "")
            val name = if(_currencies.value?.data?.get(currencyKey) == null ) currencyKey else _currencies.value?.data?.get(currencyKey)
            Quote(name!!, converted)
        }.sortedBy { it.key }

    }
}