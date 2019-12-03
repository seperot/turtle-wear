package uk.co.ijhdev.trtlware.repo

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.ijhdev.trtlware.network.Weather
import uk.co.ijhdev.trtlware.workers.WeatherWorker.Companion.tempString
import uk.co.ijhdev.trtlware.workers.WeatherWorker.Companion.weatherString

/**
 * Created by Seperot on 26/03/2018.
 */

class CurrentWeatherFinder {

  private fun getLatestWeather(lat: String, lon: String): Call<Weather.WeatherValues> {
      return Weather.GetWeather.create().getCurrentWeather(lat, lon)
  }

  fun getWeatherValues(lat: String, lon: String, temp: String) {
    try {
      getLatestWeather(lat, lon).enqueue(object : Callback<Weather.WeatherValues> {
          override fun onFailure(call: Call<Weather.WeatherValues>?, t: Throwable?) {
              Log.v("retrofit", "weather call failed")
          }

          override fun onResponse(call: Call<Weather.WeatherValues>?, response: Response<Weather.WeatherValues>?) {
              response?.body()?.let {
                  weatherString = "w" + it.icon
                  tempString = if(temp == "Fahrenheit") {
                      (it.temp!!.toFloat() * 9.0f/5.0f + 32).toString() + " °F"
                  } else {
                      it.temp + " °C"
                  }
              }
          }
      })
  } catch (exception : Exception) { /*not used */}
    weatherString = "err"
    tempString = "err"
  }
}