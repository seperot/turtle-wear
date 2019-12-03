package uk.co.ijhdev.trtlware.settings.items

/**
 * Created by Seperot on 02/12/2019.
 */
class CurrencyListsItem(itemId: Int) : Item {
  private val mItemId: Int = itemId

  override fun getItemId(): Int {
    return mItemId
  }
}