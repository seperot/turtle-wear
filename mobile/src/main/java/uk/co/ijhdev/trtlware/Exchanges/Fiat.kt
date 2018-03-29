package uk.co.ijhdev.trtlware.Exchanges

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class Fiat {

    class Currency {

        @SerializedName("buy")
        var buy: Double? = null
        @SerializedName("sell")
        var sell: Double? = null
        @SerializedName("last")
        var last: Double? = null
        @SerializedName("15m")
        var price15m: Double? = null
        @SerializedName("symbol")
        var symbol: String? = null

    }

    class CurrencyRes {
        @SerializedName("USD")
        var usd: Currency? = null
        @SerializedName("GBP")
        var gbp: Currency? = null
        @SerializedName("CAD")
        var cad: Currency? = null
        @SerializedName("EUR")
        var eur: Currency? = null
        @SerializedName("RUB")
        var rub: Currency? = null

    }

    interface ApiInterface {
        @GET("/ticker")
         fun getCategoryDetails(): Call<CurrencyRes>

        companion object Factory {
            val BASE_URL = "https://blockchain.info"
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

