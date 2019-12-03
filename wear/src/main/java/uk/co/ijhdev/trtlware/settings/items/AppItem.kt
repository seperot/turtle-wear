package uk.co.ijhdev.trtlware.settings.items

import android.app.Activity
import android.content.Context
import android.content.Intent
import uk.co.ijhdev.trtlware.settings.ListSelectionActivity.Companion.LIST_TYPE

/**
 * Created by Seperot on 02/12/2019.
 */

class AppItem(itemName: String, imageId: Int, clazz: Class<out Activity?>) {
  private val mItemName: String = itemName
  private val mImageId: Int = imageId
  private val mViewType: Int = Constants.NORMAL
  private val mClass: Class<*> = clazz

  fun getItemName(): String {
    return mItemName
  }

  fun getImageId(): Int {
    return mImageId
  }

  fun getViewType(): Int {
    return mViewType
  }

  fun launchActivity(context: Context) {
    val intent = Intent(context, mClass)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.putExtra(LIST_TYPE, mItemName)
    context.startActivity(intent)
  }
}
