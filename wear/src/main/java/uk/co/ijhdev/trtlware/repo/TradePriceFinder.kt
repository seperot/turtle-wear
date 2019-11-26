package uk.co.ijhdev.trtlware.repo

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.ijhdev.trtlware.network.Prices
import uk.co.ijhdev.trtlware.workers.TurtlePriceWorker.Companion.currentString

/**
 * Created by Seperot on 26/03/2018.
 */

class TradePriceFinder {

  private fun getLatestValues(): Call<Prices.CurrencyValues> {
    val help =  Prices.GetCurrency.create().getAllCurrency()
    return help
  }

  fun getValue(type : String) {
    try {
        getLatestValues().enqueue(object : Callback<Prices.CurrencyValues> {
          override fun onFailure(call: Call<Prices.CurrencyValues>?, t: Throwable?) {
            Log.v("retrofit", "price call failed")
          }

          override fun onResponse(call: Call<Prices.CurrencyValues>?, response: Response<Prices.CurrencyValues>?) {
            response?.body()?.let {
              val trtl = "1 trtl = "
              currentString = when (type) {
                "BTC" -> trtl + it.btc
                "USD" -> trtl + it.usd
                else -> trtl + it.btc
              }
            }
          }
        })
    } catch (exception : Exception) { /*not used */}
    currentString = "1 trtl = 1 trtl"
  }
}