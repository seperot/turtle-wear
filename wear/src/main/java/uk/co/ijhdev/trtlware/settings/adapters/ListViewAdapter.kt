package uk.co.ijhdev.trtlware.settings.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import uk.co.ijhdev.trtlware.R
import uk.co.ijhdev.trtlware.settings.items.CurrencyListsItem

/**
 * Created by Seperot on 02/12/2019.
 */
class ListViewAdapter(context: Context, items: List<CurrencyListsItem>) :
    ArrayAdapter<CurrencyListsItem>(context, R.layout.list_item_layout, items) {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val mItems: List<CurrencyListsItem> = items

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var getConvertView = convertView
        val holder: Holder
        if (getConvertView == null) {
            getConvertView = mInflater.inflate(R.layout.list_item_layout, parent, false)
            holder =
                Holder()
            holder.mTextView = getConvertView?.findViewById(R.id.item_text)
            holder.mImageView = getConvertView?.findViewById(R.id.item_image)
            getConvertView?.tag = holder // Cache the holder for future use.
        } else {
            holder = getConvertView.tag as Holder
        }
        holder.mTextView?.setText(mItems[position]!!.getItemId())
        return getConvertView!!
    }

    private class Holder {
        var mTextView: TextView? = null
        var mImageView: ImageView? = null
    }

}
