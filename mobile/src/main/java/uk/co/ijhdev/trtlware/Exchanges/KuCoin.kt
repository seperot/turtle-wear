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

    class Rates {

        @SerializedName("initialprice")
        var initialprice: String? = null
        @SerializedName("last")
        var price: String? = null
        @SerializedName("high")
        var high: String? = null
        @SerializedName("low")
        var low: String? = null
        @SerializedName("vol")
        var volume: String? = null
        @SerializedName("buy")
        var bid: String? = null
        @SerializedName("sell")
        var ask: String? = null
    }

    interface ApiInterface {
        @GET("market/stats?symbol=TRTL-BTC")
        fun getCategoryDetails(): Call<Rates>

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