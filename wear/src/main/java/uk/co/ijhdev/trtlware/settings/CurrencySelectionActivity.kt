package uk.co.ijhdev.trtlware.settings

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.wear.ambient.AmbientModeSupport
import kotlinx.android.synthetic.main.activity_lists.*
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import uk.co.ijhdev.trtlware.R
import uk.co.ijhdev.trtlware.settings.adapters.ListViewAdapter
import uk.co.ijhdev.trtlware.settings.items.CurrencyListsItem
import java.util.*


/**
 * Created by Seperot on 02/12/2019.
 */
class CurrencySelectionActivity : FragmentActivity(),
    AmbientModeSupport.AmbientCallbackProvider {
    private val context : Activity = this
    private var mItems: MutableList<CurrencyListsItem>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lists)
        AmbientModeSupport.attach(this)
        mItems = ArrayList()
        mItems?.add(CurrencyListsItem(R.string.list_item_usd, getString(R.string.list_item_usd)))
        mItems?.add(CurrencyListsItem(R.string.list_item_btc, getString(R.string.list_item_btc)))
        list_view_lists.adapter =
            ListViewAdapter(
                this,
                mItems!!
            )
        val inflater = LayoutInflater.from(this)
        val titleLayout: View = inflater.inflate(R.layout.title_layout, null)
        val titleView = titleLayout.findViewById<TextView>(R.id.title_text)
        titleView.text = "Settings"
        titleView.setOnClickListener(null)
        list_view_lists.addHeaderView(titleView)

        list_view_lists.onItemClickListener =
            OnItemClickListener { _, _, position, _ ->
                mItems?.get(position - list_view_lists.headerViewsCount)
                    ?.setCurrency("test")
                EasyPermissions.requestPermissions(
                        PermissionRequest.Builder(context, 0, Context.LOCATION_SERVICE, Manifest.permission.ACCESS_FINE_LOCATION)
                                .setRationale("DO ET")
                                .setPositiveButtonText("OK")
                                .setNegativeButtonText("NAH")
                                .build())
            }
    }

    override fun getAmbientCallback(): AmbientModeSupport.AmbientCallback {
        return MyAmbientCallback()
    }

    private inner class MyAmbientCallback : AmbientModeSupport.AmbientCallback()
}