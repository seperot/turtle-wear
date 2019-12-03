package uk.co.ijhdev.trtlware.settings.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.wear.widget.WearableLinearLayoutManager.LayoutCallback

/**
 * Created by Seperot on 02/12/2019.
 */
class ScalingScrollLayoutCallback : LayoutCallback() {
  override fun onLayoutFinished(
          child: View,
          parent: RecyclerView
  ) {
    val centerOffset = child.height / 2.0f / parent.height
    val yRelativeToCenterOffset =
            child.y / parent.height + centerOffset
    var progressToCenter = Math.abs(0.5f - yRelativeToCenterOffset)
    progressToCenter = Math.min(
            progressToCenter,
            MAX_ICON_PROGRESS
    )
    child.scaleX = 1 - progressToCenter
    child.scaleY = 1 - progressToCenter
  }

  companion object {
    private const val MAX_ICON_PROGRESS = 0.65f
  }
}
