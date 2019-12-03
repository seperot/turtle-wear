package uk.co.ijhdev.trtlware.network

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Created by Seperot on 26/03/2018.
 */
class Prices {

  class CurrencyValues {
    @SerializedName("USD")
    var usd: String? = null
    @SerializedName("BTC")
    var btc: String? = null
  }

  interface GetCurrency {
    @GET("/coin")
    fun getAllCurrency(): Call<CurrencyValues>

    companion object Factory {
      private const val BASE_URL = "http://api.ijhdev.co.uk"
      fun create(): GetCurrency {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create(GetCurrency::class.java)
      }
    }
  }
}

