package uk.co.ijhdev.trtlware.Exchanges

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class Altex {

    class Coins {
        @SerializedName("BTC_TRTL")
        var rates: Rates? = null    }

    class Rates {
        @SerializedName("BTC")
        var BTC: String? = null
        @SerializedName("volume")
        var volume: String? = null
        @SerializedName("last")
        var last: String? = null
        @SerializedName("bid")
        var bid: String? = null
        @SerializedName("ask")
        var ask: String? = null
        @SerializedName("high24")
        var high24: String? = null
        @SerializedName("low24")
        var low24: String? = null
        @SerializedName("change")
        var change: String? = null
    }

    interface ApiInterface {
        @GET("ticker")
        fun getCategoryDetails(): Call<Coins>

        companion object Factory {
            val BASE_URL = "https://api.altex.exchange/v1/"
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
