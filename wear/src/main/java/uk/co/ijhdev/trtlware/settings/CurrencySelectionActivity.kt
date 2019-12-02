package uk.co.ijhdev.trtlware.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.wear.ambient.AmbientModeSupport
import kotlinx.android.synthetic.main.activity_lists.*
import uk.co.ijhdev.trtlware.R
import uk.co.ijhdev.trtlware.settings.items.CurrencyListsItem
import uk.co.ijhdev.trtlware.settings.adapters.ListViewAdapter
import java.util.*

/**
 * Created by Seperot on 02/12/2019.
 */
class CurrencySelectionActivity : FragmentActivity(),
    AmbientModeSupport.AmbientCallbackProvider {
    private var mItems: MutableList<CurrencyListsItem>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lists)
        AmbientModeSupport.attach(this)
        // Create a list of items for adapter to display.
        mItems = ArrayList()
        mItems?.add(CurrencyListsItem(R.string.list_item_usd, getString(R.string.list_item_usd)))
        mItems?.add(CurrencyListsItem(R.string.list_item_btc, getString(R.string.list_item_btc)))
        list_view_lists.adapter =
            ListViewAdapter(
                this,
                mItems!!
            )
        // Set header of listView to be the title from title_layout.
        val inflater = LayoutInflater.from(this)
        val titleLayout: View = inflater.inflate(R.layout.title_layout, null)
        val titleView = titleLayout.findViewById<TextView>(R.id.title_text)
        titleView.text = "Settings"
        titleView.setOnClickListener(null) // make title non-clickable.
        list_view_lists.addHeaderView(titleView)
        // Goes to a new screen when you click on one of the list items.
// Dependent upon position of click.
        list_view_lists.onItemClickListener =
            OnItemClickListener { _, _, position, _ ->
                mItems?.get(position - list_view_lists.headerViewsCount)
                    ?.setCurrency("test")
            }
    }

    override fun getAmbientCallback(): AmbientModeSupport.AmbientCallback {
        return MyAmbientCallback()
    }

    private inner class MyAmbientCallback : AmbientModeSupport.AmbientCallback()
}