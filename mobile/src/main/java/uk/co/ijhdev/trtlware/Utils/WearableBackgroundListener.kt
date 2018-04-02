package uk.co.ijhdev.trtlware.Utils

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.BatteryManager
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.awareness.Awareness
import com.google.android.gms.awareness.state.Weather
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.*
import uk.co.ijhdev.trtlware.R
import kotlin.math.roundToInt

/**
 * Created by Seperot on 28/03/2018.
 */

class WearableBackgroundListener : WearableListenerService() {
    lateinit var googleApiClient: GoogleApiClient
    val putDataMapReq = PutDataMapRequest.create("/trtlwear")
    lateinit var prefs: SharedPreferences
    val PREFS_FILENAME = "uk.co.ijhdev.trtlware.prefs"
    val cur = "currency"
    val exc = "exchange"
    val tem = "temperature"
    var tradePriceFinder = TradePriceFinder()
    val local_updates_timer : Long = 60000
    val web_updates_timer : Long = 600000


    override fun onCreate() {
        super.onCreate()
        prefs = this.getSharedPreferences(PREFS_FILENAME, 0)
        googleApiClient = GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addApi(Awareness.API)
                .build()
        googleApiClient.connect()
        tradePriceFinder = TradePriceFinder(cur,exc,prefs,putDataMapReq)
        runUpdates()
        runTradeUpdate()
    }

    private val handler = Handler()
    private val runnable = Runnable { runUpdates() }
    private val runnableTrade = Runnable { runTradeUpdate() }

    private fun runUpdates() {
        getBatteryLevel()
        getWeather()
        handler.postDelayed(runnable, local_updates_timer)
    }

    private fun runTradeUpdate() {
        tradePriceFinder.getExchangeValue()
        tradePriceFinder.getValue()
        handler.postDelayed(runnableTrade, web_updates_timer)
    }

    override fun onDataChanged(dataEvents: DataEventBuffer?) {
    }

    private fun getBatteryLevel() {
        val bm = getSystemService(AppCompatActivity.BATTERY_SERVICE) as BatteryManager
        var batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        putDataMapReq.getDataMap().putString("Bat_Power", batLevel.toString() + " %")
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
                            if (prefs!!.getString(tem, "") == getString(R.string.celsius)) {
                                temperature = weather.getTemperature(Weather.CELSIUS).roundToInt().toString() + "c"
                            }
                            putDataMapReq.getDataMap().putString("weather_temp", temperature)
                            putDataMapReq.getDataMap().putString("weather_type",  conditions[0].toString ())
                            val putDataReq = putDataMapReq.asPutDataRequest()
                            Wearable.DataApi.putDataItem(googleApiClient, putDataReq)
                        }

                    }
        }
    }

    override fun onMessageReceived(messageEvent: MessageEvent?) {

    }
    companion object
}