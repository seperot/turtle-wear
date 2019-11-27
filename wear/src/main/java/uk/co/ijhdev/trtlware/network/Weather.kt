package uk.co.ijhdev.trtlware.network

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * Created by Seperot on 26/03/2018.
 */
class Weather {

    class WeatherValues {
        @SerializedName("Temp")
        var temp: String? = null
        @SerializedName("Icon")
        var icon: String? = null
    }

    interface GetWeather {
        @GET("/weather")
        fun getCurrentWeather(@Header("lat") latitude: String, @Header("lon") longitude: String): Call<WeatherValues>

        companion object Factory {
            private const val BASE_URL = "http://api.ijhdev.co.uk"
            fun create(): GetWeather {
                val retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                return retrofit.create(GetWeather::class.java)
            }
        }
    }
}

