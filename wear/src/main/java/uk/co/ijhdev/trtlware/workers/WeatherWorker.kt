package uk.co.ijhdev.trtlware.workers

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import pub.devrel.easypermissions.EasyPermissions
import uk.co.ijhdev.trtlware.repo.CurrentWeatherFinder
import uk.co.ijhdev.trtlware.settings.SharedPreferenceHandler


/**
 * Created by Seperot on 28/03/2018.
 */

class WeatherWorker {

  private var currentWeatherFinder = CurrentWeatherFinder()

  @SuppressLint("MissingPermission")
  fun getWeather(context: Context) {
    if (getContext == null) getContext = context
    val packageManager = context.packageManager
    if (packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS))
      getContext?.let {
        val lm = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        if (EasyPermissions.hasPermissions(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
          val location = lm?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
          val latitude = location?.latitude.toString()
          val longitude = location?.longitude.toString()
          SharedPreferenceHandler().getTempType(context)?.let { it1 ->
            currentWeatherFinder.getWeatherValues(latitude, longitude, it1)
          }
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