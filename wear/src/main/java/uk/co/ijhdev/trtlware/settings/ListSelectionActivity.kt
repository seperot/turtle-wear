package uk.co.ijhdev.trtlware.settings

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.wear.ambient.AmbientModeSupport
import kotlinx.android.synthetic.main.activity_lists.*
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import uk.co.ijhdev.trtlware.R
import uk.co.ijhdev.trtlware.settings.adapters.ListViewAdapter
import uk.co.ijhdev.trtlware.settings.items.CurrencyListsItem
import uk.co.ijhdev.trtlware.workers.TurtlePriceWorker
import uk.co.ijhdev.trtlware.workers.WeatherWorker
import java.util.*

/**
 * Created by Seperot on 02/12/2019.
 */
class ListSelectionActivity : FragmentActivity(),
        AmbientModeSupport.AmbientCallbackProvider {
  private val context: Activity = this
  private var mItems: MutableList<CurrencyListsItem>? = null
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_lists)
    AmbientModeSupport.attach(this)
    mItems = ArrayList()
    val bundle = intent.extras
    bundle?.let {
      when(bundle.getString(LIST_TYPE)) {
        getString(R.string.preferences) -> permissionsList()
        getString(R.string.currency) -> currencyList()
        getString(R.string.temparature) -> temperatureList()
      }
    }
  }

  private fun currencyList() {
    mItems?.add(CurrencyListsItem(R.string.list_item_usd))
    mItems?.add(CurrencyListsItem(R.string.list_item_btc))
    list_view_lists.adapter =
            ListViewAdapter(this, mItems!!)
    val inflater = LayoutInflater.from(this)
    val titleLayout: View = inflater.inflate(R.layout.title_layout, null)
    val titleView = titleLayout.findViewById<TextView>(R.id.title_text)
    titleView.text = getString(R.string.select_currency)
    titleView.setOnClickListener(null)
    list_view_lists.addHeaderView(titleView)

    list_view_lists.onItemClickListener =
            OnItemClickListener { _, _, position, _ ->
              val sharedPref = SharedPreferenceHandler()
              if (position == 1) {
                sharedPref.saveCoinType(context, getString(R.string.list_item_usd))
                Toast.makeText(context, "Price set to USD", Toast.LENGTH_SHORT).show()
              } else if (position == 2) {
                sharedPref.saveCoinType(context, getString(R.string.list_item_btc))
                Toast.makeText(context, "Price set to BTC", Toast.LENGTH_SHORT).show()
              }
              TurtlePriceWorker().runTradeUpdate(context)
            }
  }

  private fun permissionsList() {
    mItems?.add(CurrencyListsItem(R.string.receive_complication_data))
    mItems?.add(CurrencyListsItem(R.string.location))
    list_view_lists.adapter = ListViewAdapter(this, mItems!!)
    val inflater = LayoutInflater.from(this)
    val titleLayout: View = inflater.inflate(R.layout.title_layout, null)
    val titleView = titleLayout.findViewById<TextView>(R.id.title_text)
    titleView.text = getString(R.string.enable_permissions)
    titleView.setOnClickListener(null)
    list_view_lists.addHeaderView(titleView)

    list_view_lists.onItemClickListener =
            OnItemClickListener { _, _, position, _ ->
              if(position == 1) {
                Toast.makeText(context, "To toggle this permission, you need to go to the App Settings on your device", Toast.LENGTH_LONG).show()
              } else if (position == 2) {
                if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                  Toast.makeText(context, "Location already allowed", Toast.LENGTH_SHORT).show()
                  return@OnItemClickListener
                }
                EasyPermissions.requestPermissions(
                  PermissionRequest.Builder(context, 0, Context.LOCATION_SERVICE, Manifest.permission.ACCESS_FINE_LOCATION)
                    .setRationale("Location data allows the watch face to get the weather for you")
                    .setPositiveButtonText("OK")
                    .setNegativeButtonText("Cancel")
                    .build())
              }
              }
  }

  private fun temperatureList() {
    mItems?.add(CurrencyListsItem(R.string.celsius))
    mItems?.add(CurrencyListsItem(R.string.fahrenheit))
    list_view_lists.adapter = ListViewAdapter(this, mItems!!)
    val inflater = LayoutInflater.from(this)
    val titleLayout: View = inflater.inflate(R.layout.title_layout, null)
    val titleView = titleLayout.findViewById<TextView>(R.id.title_text)
    titleView.text = getString(R.string.select_temparature)
    titleView.setOnClickListener(null)
    list_view_lists.addHeaderView(titleView)

    list_view_lists.onItemClickListener =
            OnItemClickListener { _, _, position, _ ->
              val sharedPref = SharedPreferenceHandler()
              if (position == 1) {
                sharedPref.saveTempType(context, getString(R.string.celsius))
                Toast.makeText(context, "Temperature set to Celsius", Toast.LENGTH_SHORT).show()
              } else if (position == 2) {
                sharedPref.saveTempType(context, getString(R.string.fahrenheit))
                Toast.makeText(context, "Temperature set to Fahrenheit", Toast.LENGTH_SHORT).show()
              }
              WeatherWorker().getWeather(context)
            }
  }

  override fun getAmbientCallback(): AmbientModeSupport.AmbientCallback {
    return MyAmbientCallback()
  }

  private inner class MyAmbientCallback : AmbientModeSupport.AmbientCallback()
  companion object {
    const val LIST_TYPE = "_listtype"
  }
}