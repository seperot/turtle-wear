package uk.co.ijhdev.trtlware.workers

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import com.google.android.gms.common.api.GoogleApiClient
import uk.co.ijhdev.trtlware.repo.CurrentWeatherFinder
import uk.co.ijhdev.trtlware.repo.TradePriceFinder
import android.location.LocationManager
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService



/**
 * Created by Seperot on 28/03/2018.
 */

class WeatherWorker {

  private var currentWeatherFinder = CurrentWeatherFinder()
  private val mainHandler = Handler(Looper.getMainLooper())

  fun getWeather(context: Context) {
    mainHandler.post(object : Runnable {
      override fun run() {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
              && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
          val location = lm?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
          val latitude = location?.latitude.toString()
          val longitude = location?.longitude.toString()
          currentWeatherFinder.getWeatherValues(latitude, longitude)
        }
        mainHandler.postDelayed(this, 1200000)
      }
    })
  }

  companion object {
    const val ARG_TEMPERATURE = "temperature"
    var weatherString : String? = "clouds"
    var tempString : String? = "14"
  }
}