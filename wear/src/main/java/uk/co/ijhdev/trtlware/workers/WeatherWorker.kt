package uk.co.ijhdev.trtlware.workers

import android.content.SharedPreferences
import com.google.android.gms.common.api.GoogleApiClient

/**
 * Created by Seperot on 28/03/2018.
 */

class WeatherWorker {

  private lateinit var googleApiClient: GoogleApiClient
  private lateinit var prefs: SharedPreferences
  private val tem = "temperature"

  fun getWeather(): String {
//    Awareness.SnapshotApi.getWeather(googleApiClient).setResultCallback { weatherResult ->
//      if (weatherResult.status.isSuccess) {
//        val weather = weatherResult.weather
//        val conditions = weather.conditions
//        var temperature = weather.getTemperature(Weather.FAHRENHEIT).roundToInt().toString() + "f"
//        if (prefs.getString(tem, "") == "celsius") {
//          temperature = weather.getTemperature(Weather.CELSIUS).roundToInt().toString() + "c"
//        }
//      }
//    }
    return "12"
  }

  companion object {
    const val RETURN_WEATHER = "weather"
    const val ARG_TEMPERATURE = "temperature"
  }
}