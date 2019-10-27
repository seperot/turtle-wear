package uk.co.ijhdev.trtlware.Utils

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.BatteryManager
import android.os.Handler
import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.awareness.Awareness
import com.google.android.gms.awareness.state.Weather
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.*
import uk.co.ijhdev.trtlware.Activity.WareSettingsActivity
import uk.co.ijhdev.trtlware.R
import kotlin.math.roundToInt

/**
 * Created by Seperot on 28/03/2018.
 */

class WearableBackgroundListener : WearableListenerService(), CapabilityClient.OnCapabilityChangedListener {
    private lateinit var googleApiClient: GoogleApiClient
    private lateinit var prefs: SharedPreferences
    private val prefsFilename = "uk.co.ijhdev.trtlware.prefs"
    private val tem = "temperature"
    private var tradePriceFinder = TradePriceFinder()
    private val localUpdatesTimer : Long = 60000

    override fun onCreate() {
        super.onCreate()
        prefs = this.getSharedPreferences(prefsFilename, 0)
        googleApiClient = GoogleApiClient.Builder(this)
                .addApi(Awareness.API)
                .build()
        googleApiClient.connect()
        tradePriceFinder = TradePriceFinder()
        Wearable.getCapabilityClient(this)
                .addListener(this, Uri.parse("wear://"), CapabilityClient.FILTER_REACHABLE)
        runUpdates()
    }

    private val handler = Handler()
    private val runnable = Runnable { runUpdates() }

    private fun runUpdates() {
        getBatteryLevel()
        getWeather()
        runTradeUpdate()
        handler.postDelayed(runnable, localUpdatesTimer)
    }

    private fun runTradeUpdate() {
        val putDataMapRequest = PutDataMapRequest.create("/price")
        putDataMapRequest.dataMap.putString("price", "1 trtl = " + prefs.getString(WareSettingsActivity.ARG_CURRENCY, "")?.let { tradePriceFinder.getValue(it)})
        putDataToWatch(putDataMapRequest.asPutDataRequest())
    }

    override fun onDataChanged(dataEvents: DataEventBuffer?) {/*Not Used*/}

    private fun getBatteryLevel() {
        val bm = getSystemService(AppCompatActivity.BATTERY_SERVICE) as BatteryManager
        val batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        val putDataMapRequest = PutDataMapRequest.create("/phoneBattery")
        putDataMapRequest.dataMap.putString("Bat_Power", "$batLevel %")
        putDataToWatch(putDataMapRequest.asPutDataRequest())
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
                            val putDataMapRequest = PutDataMapRequest.create("/weather")
                            putDataMapRequest.dataMap.putString("weather_temp", temperature)
                            putDataMapRequest.dataMap.putString("weather_type",  conditions[0].toString ())
                            putDataToWatch(putDataMapRequest.asPutDataRequest())
                        }
                    }
        }
    }

    private fun putDataToWatch(request : PutDataRequest) {
        val dataClient  = Wearable.getDataClient(this)
        val task = dataClient.putDataItem(request)
        Tasks.await(task)
    }

    override fun onMessageReceived(messageEvent: MessageEvent?) {/* not used */}
}