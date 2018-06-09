package uk.co.ijhdev.trtlware.Utils

import android.content.SharedPreferences
import android.util.Log
import com.google.android.gms.wearable.PutDataMapRequest
import retrofit2.Call
import retrofit2.Callback
import uk.co.ijhdev.trtlware.Exchanges.Altex
import uk.co.ijhdev.trtlware.Exchanges.Fiat
import uk.co.ijhdev.trtlware.Exchanges.TradeOgre
import uk.co.ijhdev.trtlware.Exchanges.TradeSatoshi
import java.text.NumberFormat

/**
 * Created by Seperot on 26/03/2018.
 */


class TradePriceFinder(val cur : String? = null, val exc : String? = null, val prefs: SharedPreferences? = null, val putDataMapReq: PutDataMapRequest? = null) {

    var btcval : Float = 0f

    fun getValue() {
        if(prefs!!.getString(cur, "") != "BTC") {
            fiatPrice()
        }
        else {
            putDataMapReq?.getDataMap()?.putString("price", "1 trtl = BTC " + btcval)
        }
    }

    fun currencySelector(response: retrofit2.Response<Fiat.CurrencyRes>?) : Float {
        when(prefs!!.getString(cur, "")){
            "USD" -> return response?.body()?.usd?.last!!.toFloat()
            "GBP" -> return response?.body()?.gbp?.last!!.toFloat()
            "CAD" -> return response?.body()?.cad?.last!!.toFloat()
            "EUR" -> return response?.body()?.eur?.last!!.toFloat()
            "RUB" -> return response?.body()?.rub?.last!!.toFloat()
        }
        return response?.body()?.usd?.last!!.toFloat()
    }

    fun symbolGetter(response: retrofit2.Response<Fiat.CurrencyRes>?) : String? {
        when(prefs!!.getString(cur, "")){
            "USD" -> return response?.body()?.usd?.symbol
            "GBP" -> return response?.body()?.gbp?.symbol
            "CAD" -> return response?.body()?.cad?.symbol
            "EUR" -> return response?.body()?.eur?.symbol
            "RUB" -> return response?.body()?.rub?.symbol
        }
        return "BTC"
    }

    fun getExchangeValue() {
        when(prefs!!.getString(exc, "")){
            "TradeOgre" -> TradeOgre()
            "TradeSatoshi" -> TradeSatoshi()
            "Altex" -> Altex()

        }
    }

    fun fiatPrice ()  {
        val apiService = Fiat.ApiInterface.create()
        val call = apiService.getCategoryDetails()
        var value: Float
        call.enqueue(object : Callback<Fiat.CurrencyRes> {
            override fun onResponse(call: Call<Fiat.CurrencyRes>, response: retrofit2.Response<Fiat.CurrencyRes>?) {
                if (response != null) {
                    value = currencySelector(response)
                    val oneSat = btcval * value
                    Log.d("HUH", oneSat.toString())

                    val nf = NumberFormat.getInstance()
                    nf.maximumFractionDigits = 6
                    nf.isGroupingUsed = false
                    if(btcval == 0f) {
                        putDataMapReq?.getDataMap()?.putString("price", "1 trtl = 1 trtl")
                        Log.d("PRICE", "Price 0")
                        return
                    }
                    putDataMapReq?.getDataMap()?.putString("price", "1 trtl = " + symbolGetter(response) + nf.format(oneSat))
                    Log.d("PRICE", symbolGetter(response) + nf.format(oneSat))
                }
            }
            override fun onFailure(call: Call<Fiat.CurrencyRes>, t: Throwable) {
                putDataMapReq?.getDataMap()?.putString("price", "1 trtl = 1 trtl")
                Log.d("PRICE", "Failed to collect price")
            }
        })
    }

    fun TradeOgre() {

        val apiService = TradeOgre.ApiInterface.create()
        val call = apiService.getCategoryDetails()
        call.enqueue(object : Callback<TradeOgre.Rates> {
            override fun onResponse(call: Call<TradeOgre.Rates>, response: retrofit2.Response<TradeOgre.Rates>?) {
                if (response != null) {
                    btcval = response.body()?.price!!.toFloat()
                }
            }
            override fun onFailure(call: Call<TradeOgre.Rates>, t: Throwable) {
                btcval = 0f
            }
        })
    }

    fun TradeSatoshi() {
        val apiService = TradeSatoshi.ApiInterface.create()
        val call = apiService.getCategoryDetails()
        call.enqueue(object : Callback<TradeSatoshi.Connected> {
            override fun onResponse(call: Call<TradeSatoshi.Connected>, response: retrofit2.Response<TradeSatoshi.Connected>?) {
                if (response != null) {
                    btcval = response.body()?.result?.last!!.toFloat()
                }
            }
            override fun onFailure(call: Call<TradeSatoshi.Connected>, t: Throwable) {
                btcval = 0f
            }
        })
    }

    fun Altex() {
        val apiService = Altex.ApiInterface.create()
        val call = apiService.getCategoryDetails()
        call.enqueue(object : Callback<Altex.Data> {
            override fun onResponse(call: Call<Altex.Data>, response: retrofit2.Response<Altex.Data>?) {
                if (response != null) {
                    Log.d("DOG", response.body()?.data!!.rates?.last!!.toString())
                    btcval = response.body()?.data!!.rates?.last!!.toFloat()
                }
            }
            override fun onFailure(call: Call<Altex.Data>, t: Throwable) {
                btcval = 0f
            }
        })
    }

}