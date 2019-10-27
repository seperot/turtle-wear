package uk.co.ijhdev.trtlware.Activity

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AppCompatActivity
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

    private val prefsFilename = "uk.co.ijhdev.trtlware.prefs"
    private lateinit var currency: ArrayAdapter<String>
    private lateinit var currentCurrency: String
    private lateinit var currentWeather: String
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trtlwear_settings)
        setSpinners()
        setCurrentVariables()
        setDefaults()
        val intent = Intent(applicationContext, WearableBackgroundListener::class.java)
        this.startService(intent)
    }

    private fun setDefaults() {
        currentWeather = getString(R.string.fahrenheit)
        prefs.getString(ARG_TEMPERATURE, "")?.let {
            currentWeather = it
        }
        if (prefs.getString(ARG_CURRENCY, "") == null) {
            prefs.edit().putString(ARG_CURRENCY, resources.getStringArray(R.array.currency_array_main)[1]).apply()
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 940)
        }
    }

    private fun setCurrentVariables() {
        prefs = this.getSharedPreferences(prefsFilename, 0)
        currency_spinner.setSelection(currency.getPosition(prefs.getString(ARG_CURRENCY, "")))
        if (prefs.getString(ARG_TEMPERATURE, "") == getString(R.string.celsius)) {
            toggle_temp.isChecked = true
        } else {
            prefs.edit().putString(ARG_TEMPERATURE, getString(R.string.fahrenheit)).apply()
        }
        setClickListeners()
    }

    private fun setClickListeners() {
        toggle_temp.setOnCheckedChangeListener { _, isChecked ->
            currentWeather = if (isChecked) {
                getString(R.string.celsius)
            } else {
                getString(R.string.fahrenheit)
            }
        }

        save_button.setOnClickListener {
            prefs.edit().putString(ARG_CURRENCY, currentCurrency).apply()
            prefs.edit().putString(ARG_TEMPERATURE, currentWeather).apply()
        }
    }

    private fun setSpinners() {
        currency = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.currency_array_main))
        currency_spinner.onItemSelectedListener = this
        currency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        currency_spinner.adapter = currency
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tip_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val clipboard : ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        when (item?.itemId) {
            R.id.btc_tip -> {
                val clip = ClipData.newPlainText(getString(R.string.btc), getString(R.string.btcwallet))
                clipboard.setPrimaryClip(clip)
                Toast.makeText(applicationContext, getString(R.string.btc) + " " + getString(R.string.wallettoast), Toast.LENGTH_LONG).show()
            }
            R.id.trtl_tip -> {
                val clip = ClipData.newPlainText(getString(R.string.trtl), getString(R.string.trtlwallet))
                clipboard.setPrimaryClip(clip)
                Toast.makeText(applicationContext, getString(R.string.trtl) + " " + getString(R.string.wallettoast), Toast.LENGTH_LONG).show()
            }
        }
        return true
    }

    override fun onNothingSelected(p0: AdapterView<*>?) { }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (p0 == currency_spinner) {
            currentCurrency = resources.getStringArray(R.array.currency_array_main)[p2]
        }
    }

    companion object {
        const val ARG_CURRENCY = "currency"
        const val ARG_TEMPERATURE = "temperature"
    }
}

