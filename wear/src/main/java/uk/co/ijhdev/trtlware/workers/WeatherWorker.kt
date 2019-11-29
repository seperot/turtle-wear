package uk.co.ijhdev.trtlware.workers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import uk.co.ijhdev.trtlware.repo.CurrentWeatherFinder


/**
 * Created by Seperot on 28/03/2018.
 */

class WeatherWorker {

  private var currentWeatherFinder = CurrentWeatherFinder()
  private val mainHandler = Handler(Looper.getMainLooper())

  fun getWeather(context: Context) {
        if (getContext == null) getContext = context
        getContext?.let {
          val lm = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
          if (ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                  && ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val location = lm?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val latitude = location?.latitude.toString()
            val longitude = location?.longitude.toString()
            currentWeatherFinder.getWeatherValues(latitude, longitude)
          }
        }
  }

  companion object {
    var getContext: Context? = null
    const val ARG_TEMPERATURE = "temperature"
    var weatherString: String? = "clouds"
    var tempString: String? = "14"
  }
}