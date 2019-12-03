package uk.co.ijhdev.trtlware

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.wear.ambient.AmbientModeSupport
import androidx.wear.widget.WearableLinearLayoutManager
import androidx.wear.widget.WearableRecyclerView
import uk.co.ijhdev.trtlware.settings.ListSelectionActivity
import uk.co.ijhdev.trtlware.settings.items.AppItem
import uk.co.ijhdev.trtlware.settings.adapters.MenuRecyclerViewAdapter
import uk.co.ijhdev.trtlware.settings.adapters.ScalingScrollLayoutCallback
import java.util.*

/**
 * Created by Seperot on 02/12/2019.
 */

class MainActivity : FragmentActivity(),
        AmbientModeSupport.AmbientCallbackProvider {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    AmbientModeSupport.attach(this)
    val items: MutableList<AppItem> = ArrayList<AppItem>()
    items.add(
            AppItem(
                    getString(R.string.preferences),
                    R.drawable.accessibility_circle,
                    ListSelectionActivity::class.java
            )
    )
    items.add(
            AppItem(
                    getString(R.string.currency),
                    R.drawable.lists_circle,
                    ListSelectionActivity::class.java
            )
    )
    items.add(
            AppItem(
                    getString(R.string.temparature),
                    R.drawable.lists_circle,
                    ListSelectionActivity::class.java
            )
    )
    val appListAdapter = MenuRecyclerViewAdapter(this, items)
    val recyclerView: WearableRecyclerView =
            findViewById(R.id.main_recycler_view)

    val scalingScrollLayoutCallback = ScalingScrollLayoutCallback()
    recyclerView.layoutManager = WearableLinearLayoutManager(this, scalingScrollLayoutCallback)
    recyclerView.isEdgeItemsCenteringEnabled = true
    recyclerView.adapter = appListAdapter
  }

  override fun getAmbientCallback(): AmbientModeSupport.AmbientCallback {
    return MyAmbientCallback()
  }

  private inner class MyAmbientCallback : AmbientModeSupport.AmbientCallback()
}