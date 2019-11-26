package uk.co.ijhdev.trtlware.repo

import retrofit2.Call
import uk.co.ijhdev.trtlware.network.Weather

/**
 * Created by Seperot on 26/03/2018.
 */

class CurrentWeatherFinder {

  private fun getLatestWeather(lat: String, lon: String): Call<Weather.WeatherValues> {
    val help =  Weather.GetWeather.create().getCurrentWeather(lat, lon)
    return help
  }

  fun getWeatherValues(lat: String, lon: String) : Array<String?> {
    try {
        val prefs = getLatestWeather(lat, lon).execute()
        prefs?.body()?.let {
          return arrayOf(it.temp, it.icon)
        }
    } catch (exception : Exception) { /*not used */}
    return  arrayOf("err", "err")
  }
}