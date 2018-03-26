package uk.co.ijhdev.trtlware.Activity

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.trtlwear_settings.*
import uk.co.ijhdev.trtlware.R
import java.util.*

class WareSettingsActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trtlwear_settings)
        setSpinners()
        setCurrentVarables()
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

}

