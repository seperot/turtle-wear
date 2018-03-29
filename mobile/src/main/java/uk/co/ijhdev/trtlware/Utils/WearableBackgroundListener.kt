package uk.co.ijhdev.trtlware.Utils

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.icu.util.ULocale
import android.os.BatteryManager
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.awareness.Awareness
import com.google.android.gms.awareness.state.Weather
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.*
import retrofit2.Call
import retrofit2.Callback
import uk.co.ijhdev.trtlware.Activity.WareSettingsActivity
import uk.co.ijhdev.trtlware.Exchanges.Fiat
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt


/**
 * Created by IJHDev on 28/03/2018.
 */
class WearableBackgroundListener : WearableListenerService() {
    lateinit var googleApiClient: GoogleApiClient
    val putDataMapReq = PutDataMapRequest.create("/trtlwear")
    lateinit var prefs: SharedPreferences
    val PREFS_FILENAME = "uk.co.ijhdev.trtlware.prefs"
    val cur = "currency"
    val exc = "exchange"
    val tem = "temperature"


    override fun onCreate() {
        super.onCreate()
        prefs = this.getSharedPreferences(PREFS_FILENAME, 0)
        googleApiClient = GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addApi(Awareness.API)
                .build()
        googleApiClient.connect()
        runUpdates()
    }

    private val handler = Handler()
    private val runnable = Runnable { runUpdates() }

    private fun runUpdates() {
        getBatteryLevel()
        getWeather()
        getValue()
        handler.postDelayed(runnable, 300000)
    }

    override fun onDataChanged(dataEvents: DataEventBuffer?) {
        LOGD(TAG, "onDataChanged: " + dataEvents!!)

        for (event in dataEvents) {
            val uri = event.dataItem.uri
            val path = uri.path
            if (COUNT_PATH == path) {
                val nodeId = uri.host
                val payload = uri.toString().toByteArray()
                Wearable.MessageApi.sendMessage(googleApiClient, nodeId, DATA_ITEM_RECEIVED_PATH,
                        payload)
            }
        }
    }

    fun getBatteryLevel() {
        val bm = getSystemService(AppCompatActivity.BATTERY_SERVICE) as BatteryManager
        var batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        putDataMapReq.getDataMap().putString("Bat_Power", batLevel.toString() + " %")
        val putDataReq = putDataMapReq.asPutDataRequest()
        Wearable.DataApi.putDataItem(googleApiClient, putDataReq)
    }

    fun getWeather() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        } else {
            Awareness.SnapshotApi.getWeather(googleApiClient)
                    .setResultCallback { weatherResult ->
                        if (weatherResult.status.isSuccess) {
                            val weather = weatherResult.weather
                            val conditions = weather.conditions
                            var temperature = weather.getTemperature(Weather.FAHRENHEIT).roundToInt().toString() + "f"
                            if (prefs!!.getString(tem, "") == "celsius") {
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

    fun getValue() {
        if(prefs!!.getString(cur, "") != "BTC") {
            fiatPrice()
        }
    }

    fun fiatPrice ()  {
        val apiService = Fiat.ApiInterface.create()
        val call = apiService.getCategoryDetails()
        var value: Float
        call.enqueue(object : Callback<Fiat.CurrencyRes> {
            override fun onResponse(call: Call<Fiat.CurrencyRes>, response: retrofit2.Response<Fiat.CurrencyRes>?) {
                if (response != null) {
                    value = response.body()?.usd?.last!!.toFloat()
                    val oneSat = value / 100000000
                    val nf = NumberFormat.getInstance()
                    nf.maximumFractionDigits = 6
                    nf.isGroupingUsed = false
                    putDataMapReq.getDataMap().putString("price", "1 trtl = " + response.body()?.usd?.symbol + nf.format(oneSat))
                }

            }

            override fun onFailure(call: Call<Fiat.CurrencyRes>, t: Throwable) {

            }
        })
    }

    override fun onMessageReceived(messageEvent: MessageEvent?) {

    }

    companion object {

        private val TAG = "DataLayerService"

        private val START_ACTIVITY_PATH = "/start-activity"
        private val DATA_ITEM_RECEIVED_PATH = "/data-item-received"
        val COUNT_PATH = "/count"
        val IMAGE_PATH = "/image"
        val IMAGE_KEY = "photo"

        fun LOGD(tag: String, message: String) {
            if (Log.isLoggable(tag, Log.DEBUG)) {
                Log.d(tag, message)
            }
        }
    }
}