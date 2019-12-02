package uk.co.ijhdev.trtlware.settings.items

import android.app.Activity
import android.content.Context
import android.content.Intent

/**
 * Created by Seperot on 02/12/2019.
 */

class AppItem {
    private val mItemName: String
    private val mImageId: Int
    private val mViewType: Int
    private val mClass: Class<*>

    constructor(
        itemName: String,
        imageId: Int,
        viewType: Int,
        clazz: Class<out Activity?>
    ) {
        mItemName = itemName
        mImageId = imageId
        mViewType = viewType
        mClass = clazz
    }

    constructor(itemName: String, imageId: Int, clazz: Class<out Activity?>) {
        mItemName = itemName
        mImageId = imageId
        mViewType = Constants.NORMAL
        mClass = clazz
    }

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
        context.startActivity(intent)
    }
}
