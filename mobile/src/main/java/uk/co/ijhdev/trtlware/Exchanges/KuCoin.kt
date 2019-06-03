package uk.co.ijhdev.trtlware.Exchanges

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Created by Seperot on 26/03/2018.
 */

class KuCoin {

    class Data {
        @SerializedName("code")
        var code: String? = null
        @SerializedName("data")
        var data: Rates? = null
    }

    class Rates {
        @SerializedName("symbol")
        var symbol: String? = null
        @SerializedName("high")
        var high: String? = null
        @SerializedName("vol")
        var volume: String? = null
        @SerializedName("last")
        var last: String? = null
        @SerializedName("low")
        var low: String? = null
        @SerializedName("buy")
        var buy: String? = null
        @SerializedName("sell")
        var sell: String? = null
        @SerializedName("changePrice")
        var changePrice: String? = null
        @SerializedName("averagePrice")
        var averagePrice: String? = null
        @SerializedName("time")
        var time: String? = null
        @SerializedName("changeRate")
        var changeRate: String? = null
        @SerializedName("volValue")
        var volValue: String? = null
    }

    interface ApiInterface {
        @GET("market/stats?symbol=TRTL-BTC")
        fun getCategoryDetails(): Call<Data>

        companion object Factory {
            val BASE_URL = "https://openapi-v2.kucoin.com/api/v1/"
            fun create(): ApiInterface {
                val retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                return retrofit.create(ApiInterface::class.java)
            }
        }
    }

}