package uk.co.ijhdev.trtlware.Utils

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.BatteryManager
import android.os.Handler
import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.awareness.Awareness
import com.google.android.gms.awareness.state.Weather
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.*
import uk.co.ijhdev.trtlware.Activity.WareSettingsActivity
import uk.co.ijhdev.trtlware.R
import kotlin.math.roundToInt

/**
 * Created by Seperot on 28/03/2018.
 */

class WearableBackgroundListener : WearableListenerService() {
    private lateinit var googleApiClient: GoogleApiClient
    private val putDataMapReq = PutDataMapRequest.create("/trtlwear")
    private lateinit var prefs: SharedPreferences
    private val prefsFilename = "uk.co.ijhdev.trtlware.prefs"
    private val tem = "temperature"
    private var tradePriceFinder = TradePriceFinder()
    private val localUpdatesTimer : Long = 60000
    private val webUpdatesTimer : Long = 600000


    override fun onCreate() {
        super.onCreate()
        prefs = this.getSharedPreferences(prefsFilename, 0)
        googleApiClient = GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addApi(Awareness.API)
                .build()
        googleApiClient.connect()
        tradePriceFinder = TradePriceFinder()
        runUpdates()
        runTradeUpdate()
    }

    private val handler = Handler()
    private val runnable = Runnable { runUpdates() }
    private val runnableTrade = Runnable { runTradeUpdate() }

    private fun runUpdates() {
        getBatteryLevel()
        getWeather()
        handler.postDelayed(runnable, localUpdatesTimer)
    }

    private fun runTradeUpdate() {
        putDataMapReq.dataMap.putString("price", "1 trtl = " + prefs.getString(WareSettingsActivity.ARG_CURRENCY, "")?.let { tradePriceFinder.getValue(it)})
        handler.postDelayed(runnableTrade, webUpdatesTimer)
    }

    override fun onDataChanged(dataEvents: DataEventBuffer?) {
    }

    private fun getBatteryLevel() {
        val bm = getSystemService(AppCompatActivity.BATTERY_SERVICE) as BatteryManager
        val batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        putDataMapReq.dataMap.putString("Bat_Power", "$batLevel %")
        val putDataReq = putDataMapReq.asPutDataRequest()
        Wearable.DataApi.putDataItem(googleApiClient, putDataReq)
    }

    private fun getWeather() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        } else {
            Awareness.SnapshotApi.getWeather(googleApiClient)
                    .setResultCallback { weatherResult ->
                        if (weatherResult.status.isSuccess) {
                            val weather = weatherResult.weather
                            val conditions = weather.conditions
                            var temperature = weather.getTemperature(Weather.FAHRENHEIT).roundToInt().toString() + "f"
                            if (prefs.getString(tem, "") == getString(R.string.celsius)) {
                                temperature = weather.getTemperature(Weather.CELSIUS).roundToInt().toString() + "c"
                            }
                            putDataMapReq.dataMap.putString("weather_temp", temperature)
                            putDataMapReq.dataMap.putString("weather_type",  conditions[0].toString ())
                            val putDataReq = putDataMapReq.asPutDataRequest()
                            Wearable.DataApi.putDataItem(googleApiClient, putDataReq)
                        }

                    }
        }
    }

    override fun onMessageReceived(messageEvent: MessageEvent?) {/* not used */}
}