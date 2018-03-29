package uk.co.ijhdev.trtlware.Exchanges

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Created by Seperot on 26/03/2018.
 */

class TradeOgre {

    class Rates {

        @SerializedName("initialprice")
        var initialprice: String? = null
        @SerializedName("price")
        var price: String? = null
        @SerializedName("high")
        var high: String? = null
        @SerializedName("low")
        var low: String? = null
        @SerializedName("volume")
        var volume: String? = null
        @SerializedName("bid")
        var bid: String? = null
        @SerializedName("ask")
        var ask: String? = null
    }

    interface ApiInterface {
        @GET("ticker/BTC-TRTL")
        fun getCategoryDetails(): Call<Rates>

        companion object Factory {
            val BASE_URL = "https://tradeogre.com/api/v1/"
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