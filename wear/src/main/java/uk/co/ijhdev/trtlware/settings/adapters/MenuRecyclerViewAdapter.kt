package uk.co.ijhdev.trtlware.settings.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.co.ijhdev.trtlware.R
import uk.co.ijhdev.trtlware.settings.items.AppItem
import uk.co.ijhdev.trtlware.settings.items.Constants

/**
 * Created by Seperot on 02/12/2019.
 */
class MenuRecyclerViewAdapter(context: Context, items: List<AppItem>) : RecyclerView.Adapter<MenuRecyclerViewAdapter.Holder?>() {
    private val mContext: Context = context
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val mItems: List<AppItem> = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(mInflater.inflate(R.layout.app_item_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if (mItems.isEmpty()) {
            return
        }
        val item: AppItem = mItems[position]
        if (item.getViewType() == Constants.HEADER_FOOTER) {
            return
        }
        holder.bind(item)
        holder.itemView.setOnClickListener { mItems[position].launchActivity(mContext) }
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return mItems[position].getViewType()
    }

    class Holder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private var mTextView: TextView = itemView.findViewById(R.id.icon_text_view)
        private var mImageView: ImageView = itemView.findViewById(R.id.icon_image_view)
        fun bind(item: AppItem) {
            mTextView.text = item.getItemName()
            mImageView.setImageResource(item.getImageId())
        }
    }
}