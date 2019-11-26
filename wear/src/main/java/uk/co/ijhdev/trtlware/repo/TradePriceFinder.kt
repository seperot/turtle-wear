package uk.co.ijhdev.trtlware.repo

import retrofit2.Call
import uk.co.ijhdev.trtlware.network.Prices

/**
 * Created by Seperot on 26/03/2018.
 */

class TradePriceFinder {

  private fun getLatestValues(): Call<Prices.CurrencyValues> {
    val help =  Prices.GetCurrency.create().getAllCurrency()
    return help
  }

  fun getValue(type : String) : String? {
    try {
        val prefs = getLatestValues().execute()
        prefs?.body()?.let {
          return when (type) {
            "BTC" -> it.btc
            "USD" -> it.usd
            else -> it.btc
          }
        }
    } catch (exception : Exception) { /*not used */}
    return "1 trtl"
  }
}