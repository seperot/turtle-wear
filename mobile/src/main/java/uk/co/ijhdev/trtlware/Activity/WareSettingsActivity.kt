package uk.co.ijhdev.trtlware.Activity

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.BatteryManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.android.gms.awareness.Awareness
import com.google.android.gms.awareness.state.Weather
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import kotlinx.android.synthetic.main.trtlwear_settings.*
import uk.co.ijhdev.trtlware.R
import kotlin.math.roundToInt


class WareSettingsActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener,  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    val PREFS_FILENAME = "uk.co.ijhdev.trtlware.prefs"
    val cur = "currency"
    val exc = "exchange"
    val tem = "temperature"
    lateinit var exchange : ArrayAdapter<String>
    lateinit var currency : ArrayAdapter<String>
    lateinit var currentExchange : String
    lateinit var currentCurrency : String
    lateinit var currentWeather : String
    var prefs: SharedPreferences? = null
    lateinit var googleApiClient : GoogleApiClient
    val putDataMapReq = PutDataMapRequest.create("/trtlwear")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trtlwear_settings)
        setSpinners()
        setCurrentVarables()
        googleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .addApi(Awareness.API)
                .build()
    }

    override fun onStart() {
        super.onStart()
        currentWeather = "Celsius"
        googleApiClient.connect()
    }

    private fun getBatteryLevel() {

        val bm = getSystemService(BATTERY_SERVICE) as BatteryManager
        var batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        putDataMapReq.getDataMap().putString("Bat_Power", batLevel.toString() + " %")
        val putDataReq = putDataMapReq.asPutDataRequest()
        Wearable.DataApi.putDataItem(googleApiClient, putDataReq)
    }

    private fun getWeather() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 940)
        } else {
            Awareness.SnapshotApi.getWeather(googleApiClient)
                    .setResultCallback { weatherResult ->
                        if (weatherResult.status.isSuccess) {
                            val weather = weatherResult.weather
                            val conditions = weather.conditions
                            var temperature = weather.getTemperature(Weather.FAHRENHEIT).roundToInt().toString().substring(0, 2) + "f"
                            if (currentWeather == "Celsius") {
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

    fun setCurrentVarables(){
        prefs = this.getSharedPreferences(PREFS_FILENAME, 0)
        currency_spinner.setSelection(currency.getPosition(prefs!!.getString(cur,"")))
        exchange_spinner.setSelection(exchange.getPosition(prefs!!.getString(exc, "")))
        if(prefs!!.getString(tem, "") == "celsius") {
            toggle_temp.isChecked = true
        }
    }

    fun setSpinners() {
        exchange = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.exchange_array_main))
        currency = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.currency_array_main))
        exchange_spinner!!.onItemSelectedListener = this
        currency_spinner!!.onItemSelectedListener = this
        exchange.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        currency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        exchange_spinner!!.adapter = exchange
        currency_spinner!!.adapter = currency
    }

    override fun onNothingSelected(p0: AdapterView<*>?) { }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if(p0 == exchange_spinner) {
            currentExchange = resources.getStringArray(R.array.exchange_array_main).get(p2)
        }
        else if(p0 == currency_spinner) {
            currentCurrency = resources.getStringArray(R.array.currency_array_main).get(p2)
        }
    }

    override fun onConnected(p0: Bundle?) {
        getBatteryLevel()
        getWeather()
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.e("tag", "onConnectionSuspended");    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.e("tag", "onConnectionFailed");
    }

    override fun onStop() {
        if (googleApiClient != null && googleApiClient.isConnected) {
            googleApiClient.disconnect()
        }
        super.onStop()
    }

}

