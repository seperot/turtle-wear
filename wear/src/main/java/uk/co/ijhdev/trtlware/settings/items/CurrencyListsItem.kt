package uk.co.ijhdev.trtlware.settings.items

/**
 * Created by Seperot on 02/12/2019.
 */
class CurrencyListsItem(itemId: Int, currency: String) : Item {
    private val mItemId: Int = itemId
    private val mCurrency: String = currency

    override fun getItemId(): Int {
        return mItemId
    }

    fun setCurrency(currency: String?) {
        //SETSHAREDPREFSHERE
    }

}