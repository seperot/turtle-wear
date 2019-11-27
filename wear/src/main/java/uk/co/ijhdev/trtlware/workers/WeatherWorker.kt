package uk.co.ijhdev.trtlware.workers

import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import com.google.android.gms.common.api.GoogleApiClient
import uk.co.ijhdev.trtlware.repo.CurrentWeatherFinder
import uk.co.ijhdev.trtlware.repo.TradePriceFinder

/**
 * Created by Seperot on 28/03/2018.
 */

class WeatherWorker {

  private var currentWeatherFinder = CurrentWeatherFinder()
  private val mainHandler = Handler(Looper.getMainLooper())

  fun getWeather() {
    mainHandler.post(object : Runnable {
      override fun run() {
        currentWeatherFinder.getWeatherValues("12", "-12")
        mainHandler.postDelayed(this, 1200000)
      }
    })
  }

  companion object {
    const val RETURN_WEATHER = "weather"
    const val ARG_TEMPERATURE = "temperature"
    var weatherString : String? = "clouds"
    var tempString : String? = "14"
  }
}