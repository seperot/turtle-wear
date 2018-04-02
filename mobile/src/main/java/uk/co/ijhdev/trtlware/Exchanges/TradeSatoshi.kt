package uk.co.ijhdev.trtlware.Exchanges

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Created by Seperot on 26/03/2018.
 */

class TradeSatoshi {


    class Connected {
        @SerializedName("success")
        var success: Boolean? = null
        @SerializedName("message")
        var message: String? = null
        @SerializedName("result")
        var result: Results? = null
    }

    class Results {
        @SerializedName("bid")
        var bid: Float? = null
        @SerializedName("ask")
        var ask: Float? = null
        @SerializedName("last")
        var last: Float? = null
        @SerializedName("market")
        var market: String? = null
    }

    interface ApiInterface {
        @GET("getticker?market=TRTL_BTC")
        fun getCategoryDetails(): Call<Connected>

        companion object Factory {
            val BASE_URL = "https://tradesatoshi.com/api/public/"
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