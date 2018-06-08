package uk.co.ijhdev.trtlware.Activity

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.trtlwear_settings.*
import uk.co.ijhdev.trtlware.R
import uk.co.ijhdev.trtlware.Utils.WearableBackgroundListener


/**
 * Created by Seperot on 26/03/2018.
 */

class WareSettingsActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    val PREFS_FILENAME = "uk.co.ijhdev.trtlware.prefs"
    val cur = "currency"
    val exc = "exchange"
    val tem = "temperature"
    lateinit var exchange: ArrayAdapter<String>
    lateinit var currency: ArrayAdapter<String>
    lateinit var currentExchange: String
    lateinit var currentCurrency: String
    lateinit var currentWeather: String
    lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trtlwear_settings)
        setSpinners()
        setCurrentVarables()
        setDefaults()
        val intent = Intent(applicationContext, WearableBackgroundListener::class.java)
        this.startService(intent)
    }

    private fun setDefaults() {
        currentWeather = getString(R.string.fahrenheit)
        if (prefs!!.getString(tem, "") != null) {
            currentWeather = prefs.getString(tem, "")
        }
        if (prefs!!.getString(cur, "") == null) {
            prefs.edit().putString(cur, resources.getStringArray(R.array.currency_array_main)[1]).apply()
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 940)
        }
    }

    private fun setCurrentVarables() {
        prefs = this.getSharedPreferences(PREFS_FILENAME, 0)
        currency_spinner.setSelection(currency.getPosition(prefs.getString(cur, "")))
        exchange_spinner.setSelection(exchange.getPosition(prefs.getString(exc, "")))
        if (prefs.getString(tem, "") == getString(R.string.celsius)) {
            toggle_temp.isChecked = true
        } else {
            prefs.edit().putString(tem, getString(R.string.fahrenheit)).apply()
        }
        setClickListeners()
    }

    private fun setClickListeners() {
        toggle_temp.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                currentWeather = getString(R.string.celsius)
            } else {
                currentWeather = getString(R.string.fahrenheit)
            }
        }

        save_button.setOnClickListener {
            if (currentExchange != null) {
                prefs.edit().putString(exc, currentExchange).apply()
            }
            if (currentCurrency != null) {
                prefs.edit().putString(cur, currentCurrency).apply()
            }
            if (currentWeather != null) {
                prefs.edit().putString(tem, currentWeather).apply()
            }
        }

    }

    private fun setSpinners() {
        exchange = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.exchange_array_main))
        currency = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.currency_array_main))
        exchange_spinner!!.onItemSelectedListener = this
        currency_spinner!!.onItemSelectedListener = this
        exchange.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        currency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        exchange_spinner!!.adapter = exchange
        currency_spinner!!.adapter = currency
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tip_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        when (item!!.itemId) {
        R.id.btc_tip -> {
            val clip = ClipData.newPlainText(getString(R.string.btc), getString(R.string.btcwallet))
            clipboard.primaryClip = clip
            Toast.makeText(applicationContext, getString(R.string.btc) + " " + getString(R.string.wallettoast), Toast.LENGTH_LONG).show()
        }
        R.id.trtl_tip -> {
            val clip = ClipData.newPlainText(getString(R.string.trtl), getString(R.string.trtlwallet))
            clipboard.primaryClip = clip
            Toast.makeText(applicationContext, getString(R.string.trtl) + " " + getString(R.string.wallettoast), Toast.LENGTH_LONG).show()

        }
        }
        return true
    }

    override fun onNothingSelected(p0: AdapterView<*>?) { }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (p0 == exchange_spinner) {
            currentExchange = resources.getStringArray(R.array.exchange_array_main).get(p2)
        } else if (p0 == currency_spinner) {
            currentCurrency = resources.getStringArray(R.array.currency_array_main).get(p2)
        }
    }

}

